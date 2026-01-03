package com.hustleborn.service.service.productcategory;

import java.util.List;
import com.hustleborn.service.model.productcategory.ProductCategories;

public interface IProductCategoriesService {
    List<ProductCategories> getAllCategories();
    ProductCategories createCategory(ProductCategories category);
    ProductCategories getCategoryById(Long id);
    ProductCategories updateCategory(Long id, ProductCategories category);
    void deleteCategory(Long id);
    List<ProductCategories> getSubcategories(Long parentId);
    List<ProductCategories> getAllDescendants(Long categoryId);
    List<ProductCategories> getAllAncestors(Long categoryId);
    void populateClosureTable();
}