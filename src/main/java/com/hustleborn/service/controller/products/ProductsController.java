package com.hustleborn.service.controller.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hustleborn.service.model.products.Products;
import com.hustleborn.service.model.productvariants.ProductVariants;
import com.hustleborn.service.service.products.ProductsService;
import com.hustleborn.service.utils.exceptions.ApiException;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

	@Autowired
	private ProductsService productsService;

	@GetMapping
	public ResponseEntity<List<Products>> getAllProducts() {

		try {
			return ResponseEntity.ok(productsService.getAllProducts());
		} catch (Exception e) {
			throw new ApiException("Unable to fetch products", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<Products> saveProduct(@RequestBody Products productDetails) {
		try {
			return ResponseEntity.ok(productsService.createProduct(productDetails));
		} catch (Exception e) {
			throw new ApiException("Unable to save Products", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Products> getProduct(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(productsService.getProductWithVariants(id));
		} catch (Exception e) {
			throw new ApiException("Unable to fetch product", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{id}/variants")
	public ResponseEntity<ProductVariants> createVariant(@PathVariable Long id, @RequestBody ProductVariants variant) {
		try {
			variant.setProduct(productsService.getProductWithVariants(id)); 
			return ResponseEntity.ok(productsService.createVariant(variant));
		} catch (Exception e) {
			throw new ApiException("Unable to create variant", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}/variants")
	public ResponseEntity<List<ProductVariants>> getVariants(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(productsService.getVariantsByProductId(id));
		} catch (Exception e) {
			throw new ApiException("Unable to fetch variants", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
