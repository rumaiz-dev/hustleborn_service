package com.hustleborn.service.service.products;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hustleborn.service.model.productcategory.ProductCategories;
import com.hustleborn.service.model.products.ProductStatus;
import com.hustleborn.service.model.products.Products;
import com.hustleborn.service.model.productvariants.ProductVariants;
import com.hustleborn.service.repository.productcategory.ProductCategoriesRepository;
import com.hustleborn.service.repository.products.ProductsRepository;
import com.hustleborn.service.repository.productvariants.ProductVariantsRepository;
import com.hustleborn.service.utils.exceptions.ApiException;

@Service
public class ProductsService implements IProductsService {

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private ProductVariantsRepository productVariantsRepository;

	@Autowired
	private ProductCategoriesRepository productCategoriesRepository;

	public List<Products> getAllProducts() {
		return productsRepository.findAll();
	}

	public Products createProduct(Products productDetails) {
		if (productDetails.getName() == null || productDetails.getName().trim().isEmpty()) {
			throw new ApiException("Product name is required", null, HttpStatus.BAD_REQUEST);
		}
		if (productDetails.getPrice() == null || productDetails.getPrice() < 0) {
			throw new ApiException("Valid price is required", null, HttpStatus.BAD_REQUEST);
		}

		if (productDetails.getProductCategories() != null) {
			for (ProductCategories cat : productDetails.getProductCategories()) {
				if (cat.getId() == null || !productCategoriesRepository.existsById(cat.getId())) {
					throw new ApiException("Invalid category ID: " + cat.getId(), null, HttpStatus.BAD_REQUEST);
				}
			}
		}

		Products product = new Products();

		product.setName(productDetails.getName());

		String slug = productDetails.getSlug();
		if (slug == null || slug.trim().isEmpty()) {
			slug = generateSlug(productDetails.getName());
		}
		product.setSlug(slug);

		product.setPrice(productDetails.getPrice());
		product.setPurchasingPrice(productDetails.getPurchasingPrice());
		product.setSalePrice(productDetails.getSalePrice());
		product.setDescription(productDetails.getDescription());
		product.setProductCategories(productDetails.getProductCategories());

		String code = productDetails.getCode();
		if (code == null || code.trim().isEmpty()) {
			code = generateCode();
		}
		product.setCode(code);

		product.setStockQuantity(productDetails.getStockQuantity() != 0 ? productDetails.getStockQuantity() : 0);
		product.setStockStatus(productDetails.getStockStatus() != null ? productDetails.getStockStatus() : "in_stock");
		product.setStatus(productDetails.getStatus() != null ? productDetails.getStatus() : ProductStatus.ACTIVE);
		product.setCreatedAt(LocalDateTime.now());
		product.setParentId(productDetails.getParentId());
		product.setVariant(productDetails.getVariant());
		product.setAttributes(productDetails.getAttributes());
		product.setDimensions(productDetails.getDimensions());

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

	private String generateSlug(String name) {
		return name.toLowerCase().replaceAll("[^a-z0-9\\s]", "").replaceAll("\\s+", "-");
	}

	private String generateCode() {
		return "PROD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}

}
