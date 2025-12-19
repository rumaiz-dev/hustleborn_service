package com.hustleborn.service.service.products;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustleborn.service.model.products.Products;
import com.hustleborn.service.model.productvariants.ProductVariants;
import com.hustleborn.service.repository.products.ProductVariantsRepository;
import com.hustleborn.service.repository.products.ProductsRepository;

@Service
public class ProductsService {

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private ProductVariantsRepository productVariantsRepository;

	public List<Products> getAllProducts() {
		return productsRepository.findAll();
	}

	public Products createProduct(Products productDetails) {

		Products product = new Products();

		product.setName(productDetails.getName());
		product.setSlug(productDetails.getSlug());
		product.setSku(productDetails.getSku());
		product.setPrice(productDetails.getPrice());
		product.setSalePrice(productDetails.getSalePrice());
		product.setDescription(productDetails.getDescription());
		product.setAccountId(productDetails.getAccountId());
		product.setProductCategories(productDetails.getProductCategories());
		product.setCode(productDetails.getCode());
		product.setStockQuantity(productDetails.getStockQuantity());
		product.setStockStatus(productDetails.getStockStatus());
		product.setStatus(productDetails.getStatus());
		product.setCreatedAt(LocalDateTime.now());
		product.setParentId(productDetails.getParentId());
		product.setVariant(productDetails.getVariant());

		return productsRepository.save(product);

	}

	public Products getProductWithVariants(Long productId) {
		Products product = productsRepository.findById(productId).orElse(null);
		if (product != null) {
		}
		return product;
	}

	public ProductVariants createVariant(ProductVariants variant) {
		variant.setCreatedAt(LocalDateTime.now());
		return productVariantsRepository.save(variant);
	}

	public List<ProductVariants> getVariantsByProductId(Long productId) {
		return productVariantsRepository.findByProductId(productId);
	}

}
