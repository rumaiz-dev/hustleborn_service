package com.hustleborn.service.service.products;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hustleborn.service.model.productattributes.ProductAttributes;
import com.hustleborn.service.model.productcategory.ProductCategories;
import com.hustleborn.service.model.products.ProductStatus;
import com.hustleborn.service.model.products.ProductType;
import com.hustleborn.service.model.products.Products;
import com.hustleborn.service.model.productvariants.ProductVariants;
import com.hustleborn.service.model.productvariants.StockStatus;
import com.hustleborn.service.repository.productcategory.ProductCategoriesRepository;
import com.hustleborn.service.repository.products.ProductsRepository;
import com.hustleborn.service.repository.productvariants.ProductVariantsRepository;
import com.hustleborn.service.utils.exceptions.ApiException;

@Service
public class ProductsService {

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private ProductVariantsRepository productVariantsRepository;

	@Autowired
	private ProductCategoriesRepository productCategoriesRepository;

	private final ObjectMapper objectMapper = new ObjectMapper();

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

		ProductType productType = productDetails.getProductType();
		if (productType == null) {
			if (productDetails.getParentId() != null) {
				productType = ProductType.Variation;
			} else if (hasVariantAttributes(productDetails.getAttributes())) {
				productType = ProductType.Variable;
			} else {
				productType = ProductType.Simple;
			}
		}
		validateProductType(productType, productDetails);
		product.setProductType(productType);

		Products savedProduct = productsRepository.save(product);

		if (savedProduct.getProductType() == ProductType.Variable) {
			generateProductVariants(savedProduct);
		}

		return savedProduct;
	}

	public Products getProductWithVariants(Long productId) {
		return productsRepository.findById(productId).orElse(null);
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

	private boolean hasVariantAttributes(Map<String, ProductAttributes> attributes) {
		if (attributes == null) return false;
		return attributes.values().stream().anyMatch(attr -> Boolean.TRUE.equals(attr.getIsVariant()));
	}

	private void validateProductType(ProductType productType, Products productDetails) {
		switch (productType) {
			case Simple:
				if (productDetails.getParentId() != null) {
					throw new ApiException("Simple products cannot have a parentId", null, HttpStatus.BAD_REQUEST);
				}
				if (hasVariantAttributes(productDetails.getAttributes())) {
					throw new ApiException("Simple products cannot have variation-specific attributes", null, HttpStatus.BAD_REQUEST);
				}
				break;
			case Variable:
				if (productDetails.getParentId() != null) {
					throw new ApiException("Variable products cannot have a parentId", null, HttpStatus.BAD_REQUEST);
				}
				if (!hasVariantAttributes(productDetails.getAttributes())) {
					throw new ApiException("Variable products must have attributes with isVariant=true", null, HttpStatus.BAD_REQUEST);
				}
				break;
			case Variation:
				if (productDetails.getParentId() == null) {
					throw new ApiException("Variation products must have a parentId", null, HttpStatus.BAD_REQUEST);
				}
				Products parent = productsRepository.findById(productDetails.getParentId()).orElse(null);
				if (parent == null) {
					throw new ApiException("Parent product not found", null, HttpStatus.BAD_REQUEST);
				}
				if (parent.getProductType() != ProductType.Variable) {
					throw new ApiException("Parent product must be of type Variable", null, HttpStatus.BAD_REQUEST);
				}
				break;
		}
	}

	private void generateProductVariants(Products product) {
		Map<String, ProductAttributes> attributes = product.getAttributes();
		if (attributes == null) return;

		List<Map.Entry<String, List<String>>> variantAttrs = new ArrayList<>();
		for (Map.Entry<String, ProductAttributes> entry : attributes.entrySet()) {
			ProductAttributes attr = entry.getValue();
			if (Boolean.TRUE.equals(attr.getIsVariant())) {
				String valuesJson = attr.getAttributeValues();
				if (valuesJson != null && !valuesJson.trim().isEmpty()) {
					try {
						List<String> values = objectMapper.readValue(valuesJson, new TypeReference<List<String>>() {});
						if (!values.isEmpty()) {
							variantAttrs.add(new AbstractMap.SimpleEntry<>(attr.getAttributeName(), values));
						}
					} catch (Exception e) {
					}
				}
			}
		}

		if (variantAttrs.isEmpty()) return;

		List<List<Map.Entry<String, String>>> combinations = generateCombinations(variantAttrs);
		for (List<Map.Entry<String, String>> combo : combinations) {
			ProductVariants variant = new ProductVariants();
			variant.setProduct(product);
			variant.setCreatedAt(LocalDateTime.now());
			String sku = generateVariantSku(product.getCode(), combo);
			variant.setSku(sku);

			Map<String, String> options = new HashMap<>();
			for (Map.Entry<String, String> opt : combo) {
				options.put(opt.getKey(), opt.getValue());
			}
			try {
				variant.setVariantOptions(objectMapper.writeValueAsString(options));
			} catch (Exception e) {
			}

			variant.setStockQuantity(0);
			variant.setStockStatus(StockStatus.Instock);
			variant.setStoreId(product.getStoreId());

			productVariantsRepository.save(variant);
		}
	}

	private List<List<Map.Entry<String, String>>> generateCombinations(List<Map.Entry<String, List<String>>> attrs) {
		return cartesianProduct(attrs, 0);
	}

	private List<List<Map.Entry<String, String>>> cartesianProduct(List<Map.Entry<String, List<String>>> attrs, int index) {
		if (index == attrs.size()) {
			return List.of(new ArrayList<>());
		}

		Map.Entry<String, List<String>> current = attrs.get(index);
		String attrName = current.getKey();
		List<String> values = current.getValue();

		List<List<Map.Entry<String, String>>> rest = cartesianProduct(attrs, index + 1);
		List<List<Map.Entry<String, String>>> result = new ArrayList<>();

		for (String value : values) {
			for (List<Map.Entry<String, String>> combo : rest) {
				List<Map.Entry<String, String>> newCombo = new ArrayList<>(combo);
				newCombo.add(new AbstractMap.SimpleEntry<>(attrName, value));
				result.add(newCombo);
			}
		}

		return result;
	}

	private String generateVariantSku(String baseCode, List<Map.Entry<String, String>> combo) {
		StringBuilder sb = new StringBuilder(baseCode);
		for (Map.Entry<String, String> e : combo) {
			sb.append("-").append(e.getValue().replaceAll("[^a-zA-Z0-9]", ""));
		}
		return sb.toString();
	}

}
