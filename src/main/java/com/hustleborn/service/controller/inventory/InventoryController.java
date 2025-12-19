package com.hustleborn.service.controller.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hustleborn.service.model.inventorytransactions.InventoryTransactions;
import com.hustleborn.service.model.productvariants.ProductVariants;
import com.hustleborn.service.service.inventory.InventoryService;
import com.hustleborn.service.utils.exceptions.ApiException;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;

	@GetMapping
	public ResponseEntity<List<ProductVariants>> getAllInventory() {
		try {
			return ResponseEntity.ok(inventoryService.getAllInventory());
		} catch (Exception e) {
			throw new ApiException("Unable to fetch inventory", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/low-stock")
	public ResponseEntity<List<ProductVariants>> getLowStock() {
		try {
			return ResponseEntity.ok(inventoryService.getLowStockItems());
		} catch (Exception e) {
			throw new ApiException("Unable to fetch low stock items", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/adjust")
	public ResponseEntity<ProductVariants> adjustStock(@RequestParam Long variantId, @RequestParam int quantity,
			@RequestParam String reason, @RequestParam Long userId) {
		try {
			return ResponseEntity.ok(inventoryService.adjustStock(variantId, quantity, reason, userId));
		} catch (Exception e) {
			throw new ApiException("Unable to adjust stock", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/sale")
	public ResponseEntity<ProductVariants> recordSale(@RequestParam Long variantId, @RequestParam int quantity,
			@RequestParam String posReference, @RequestParam Long userId) {
		try {
			return ResponseEntity.ok(inventoryService.recordSale(variantId, quantity, posReference, userId));
		} catch (Exception e) {
			throw new ApiException("Unable to record sale", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/transactions/{variantId}")
	public ResponseEntity<List<InventoryTransactions>> getTransactionHistory(@PathVariable Long variantId) {
		try {
			return ResponseEntity.ok(inventoryService.getTransactionHistory(variantId));
		} catch (Exception e) {
			throw new ApiException("Unable to fetch transaction history", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}