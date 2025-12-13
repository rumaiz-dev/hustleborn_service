package com.hustleborn.service.controller.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hustleborn.service.exceptions.ApiException;
import com.hustleborn.service.model.products.Products;
import com.hustleborn.service.service.products.ProductsService;

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
}
