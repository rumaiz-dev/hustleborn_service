package com.hustleborn.service.service.productcategory;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hustleborn.service.model.productcategory.ProductCategories;
import com.hustleborn.service.model.productcategory.ProductCategoryClosure;
import com.hustleborn.service.repository.productcategory.ProductCategoriesRepository;
import com.hustleborn.service.repository.productcategory.ProductCategoryClosureRepository;
import com.hustleborn.service.utils.exceptions.ApiException;

import org.springframework.http.HttpStatus;

@Service
public class ProductCategoriesService {

    @Autowired
    private ProductCategoriesRepository productCategoriesRepository;

    @Autowired
    private ProductCategoryClosureRepository productCategoryClosureRepository;


    public List<ProductCategories> getAllCategories() {
        return productCategoriesRepository.findAll();
    }

    @Transactional
    public ProductCategories createCategory(ProductCategories category) {
 
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new ApiException("Category name is required", null, HttpStatus.BAD_REQUEST);
        }

        if (category.getSlug() == null || category.getSlug().trim().isEmpty()) {
            category.setSlug(generateSlug(category.getName()));
        }

        if (category.getParent() != null) {
            Optional<ProductCategories> parent = productCategoriesRepository.findById(category.getParent());
            if (!parent.isPresent()) {
                throw new ApiException("Parent category does not exist", null, HttpStatus.BAD_REQUEST);
            }
        }

        Optional<ProductCategories> existing = productCategoriesRepository.findAll().stream()
            .filter(c -> category.getSlug().equals(c.getSlug()))
            .findFirst();
        if (existing.isPresent()) {
            throw new ApiException("Category slug must be unique", null, HttpStatus.BAD_REQUEST);
        }

        ProductCategories savedCategory = productCategoriesRepository.save(category);
        buildClosureForCategoryAndDescendants(savedCategory.getId());
        return savedCategory;
    }


    public ProductCategories getCategoryById(Long id) {
        return productCategoriesRepository.findById(id)
            .orElseThrow(() -> new ApiException("Category not found", null, HttpStatus.NOT_FOUND));
    }

 
    @Transactional
    public ProductCategories updateCategory(Long id, ProductCategories categoryDetails) {
        ProductCategories category = getCategoryById(id);
        Long oldParent = category.getParent();

        if (categoryDetails.getName() != null) {
            category.setName(categoryDetails.getName());
        }

        if (categoryDetails.getSlug() != null) {
            // Check uniqueness
            Optional<ProductCategories> existing = productCategoriesRepository.findAll().stream()
                .filter(c -> categoryDetails.getSlug().equals(c.getSlug()) && !c.getId().equals(id))
                .findFirst();
            if (existing.isPresent()) {
                throw new ApiException("Category slug must be unique", null, HttpStatus.BAD_REQUEST);
            }
            category.setSlug(categoryDetails.getSlug());
        }

        if (categoryDetails.getParent() != null) {
            Optional<ProductCategories> parent = productCategoriesRepository.findById(categoryDetails.getParent());
            if (!parent.isPresent()) {
                throw new ApiException("Parent category does not exist", null, HttpStatus.BAD_REQUEST);
            }
            category.setParent(categoryDetails.getParent());
        }

        if (categoryDetails.getDescription() != null) {
            category.setDescription(categoryDetails.getDescription());
        }

        ProductCategories savedCategory = productCategoriesRepository.save(category);

        if (!Objects.equals(oldParent, categoryDetails.getParent())) {
            Map<Long, Integer> descendants = getDescendantsWithDepth(category.getId());
            List<Long> descIds = new ArrayList<>(descendants.keySet());
            productCategoryClosureRepository.deleteByCategoryIds(descIds);
            buildClosureForCategoryAndDescendants(category.getId());
        }

        return savedCategory;
    }

    @Transactional
    public void deleteCategory(Long id) {
        ProductCategories category = getCategoryById(id);
        // Check if has subcategories
        List<ProductCategories> subs = productCategoriesRepository.findByParent(id);
        if (!subs.isEmpty()) {
            throw new ApiException("Cannot delete category with subcategories", null, HttpStatus.BAD_REQUEST);
        }
        productCategoryClosureRepository.deleteByCategoryId(id);
        productCategoriesRepository.delete(category);
    }

    public List<ProductCategories> getSubcategories(Long parentId) {
        List<ProductCategoryClosure> closures = productCategoryClosureRepository.findDirectChildren(parentId);
        List<Long> ids = closures.stream().map(ProductCategoryClosure::getDescendantId).collect(Collectors.toList());
        return productCategoriesRepository.findAllById(ids);
    }

    public List<ProductCategories> getAllDescendants(Long categoryId) {
        List<ProductCategoryClosure> closures = productCategoryClosureRepository.findDescendantsExcludingSelf(categoryId);
        List<Long> ids = closures.stream().map(ProductCategoryClosure::getDescendantId).distinct().collect(Collectors.toList());
        return productCategoriesRepository.findAllById(ids);
    }

    public List<ProductCategories> getAllAncestors(Long categoryId) {
        List<ProductCategoryClosure> closures = productCategoryClosureRepository.findAncestors(categoryId);
        List<Long> ids = closures.stream().filter(c -> c.getDepth() > 0).map(ProductCategoryClosure::getAncestorId).distinct().collect(Collectors.toList());
        return productCategoriesRepository.findAllById(ids);
    }

    public void populateClosureTable() {
        productCategoryClosureRepository.deleteAll();
        List<ProductCategories> roots = productCategoriesRepository.findByParent(null);
        for (ProductCategories root : roots) {
            buildClosureForCategoryAndDescendants(root.getId());
        }
    }

    private void buildClosureForCategoryAndDescendants(Long categoryId) {
        Map<Long, Integer> descendants = getDescendantsWithDepth(categoryId);
        List<ProductCategoryClosure> ancestors = productCategoryClosureRepository.findAncestors(categoryId);
        List<ProductCategoryClosure> closures = new ArrayList<>();
        for (ProductCategoryClosure ancestor : ancestors) {
            for (Map.Entry<Long, Integer> desc : descendants.entrySet()) {
                closures.add(new ProductCategoryClosure(ancestor.getAncestorId(), desc.getKey(), ancestor.getDepth() + desc.getValue()));
            }
        }
        productCategoryClosureRepository.saveAll(closures);
    }

    private Map<Long, Integer> getDescendantsWithDepth(Long categoryId) {
        Map<Long, Integer> result = new HashMap<>();
        result.put(categoryId, 0);
        addDescendantsRecursive(categoryId, 0, result);
        return result;
    }

    private void addDescendantsRecursive(Long categoryId, int depth, Map<Long, Integer> result) {
        List<ProductCategories> children = productCategoriesRepository.findByParent(categoryId);
        for (ProductCategories child : children) {
            result.put(child.getId(), depth + 1);
            addDescendantsRecursive(child.getId(), depth + 1, result);
        }
    }

    private String generateSlug(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9\\s]", "").replaceAll("\\s+", "-");
    }
}