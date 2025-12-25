package com.hustleborn.service.service.products;

import java.util.List;
import com.hustleborn.service.model.products.Products;
import com.hustleborn.service.model.productvariants.ProductVariants;

public interface IProductsService {
    List<Products> getAllProducts();
    Products createProduct(Products productDetails);
    Products getProductWithVariants(Long productId);
    ProductVariants createVariant(ProductVariants variant);
    List<ProductVariants> getVariantsByProductId(Long productId);
}