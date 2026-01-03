package com.hustleborn.service.controller.productcategory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hustleborn.service.model.productcategory.ProductCategories;
import com.hustleborn.service.service.productcategory.ProductCategoriesService;
import com.hustleborn.service.utils.exceptions.ApiException;

@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoriesController {

    @Autowired
    private ProductCategoriesService productCategoriesService;

    @GetMapping
    public ResponseEntity<List<ProductCategories>> getAllCategories() {
        try {
            return ResponseEntity.ok(productCategoriesService.getAllCategories());
        } catch (Exception e) {
            throw new ApiException("Unable to fetch categories", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ProductCategories> createCategory(@RequestBody ProductCategories category) {
        try {
            return ResponseEntity.ok(productCategoriesService.createCategory(category));
        } catch (Exception e) {
            throw new ApiException("Unable to create category", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategories> getCategoryById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productCategoriesService.getCategoryById(id));
        } catch (Exception e) {
            throw new ApiException("Unable to fetch category", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategories> updateCategory(@PathVariable Long id, @RequestBody ProductCategories category) {
        try {
            return ResponseEntity.ok(productCategoriesService.updateCategory(id, category));
        } catch (Exception e) {
            throw new ApiException("Unable to update category", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            productCategoriesService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ApiException("Unable to delete category", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<ProductCategories>> getSubcategories(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productCategoriesService.getSubcategories(id));
        } catch (Exception e) {
            throw new ApiException("Unable to fetch subcategories", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/descendants")
    public ResponseEntity<List<ProductCategories>> getAllDescendants(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productCategoriesService.getAllDescendants(id));
        } catch (Exception e) {
            throw new ApiException("Unable to fetch descendants", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/ancestors")
    public ResponseEntity<List<ProductCategories>> getAllAncestors(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productCategoriesService.getAllAncestors(id));
        } catch (Exception e) {
            throw new ApiException("Unable to fetch ancestors", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/populate-closure")
    public ResponseEntity<Void> populateClosureTable() {
        try {
            productCategoriesService.populateClosureTable();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ApiException("Unable to populate closure table", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}