package com.hustleborn.service.controller.stores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hustleborn.service.model.stores.Store;
import com.hustleborn.service.service.stores.StoreService;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

	@Autowired
	private StoreService storeService;

	@GetMapping
	public ResponseEntity<List<Store>> getAllStores() {
		List<Store> stores = storeService.getAllStores();
		return ResponseEntity.ok(stores);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
		Store store = storeService.getStoreById(id);
		return ResponseEntity.ok(store);
	}

	@PostMapping
	public ResponseEntity<Store> createStore(@RequestBody Store store) {
		Store createdStore = storeService.createStore(store);
		return ResponseEntity.ok(createdStore);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Store> updateStore(@PathVariable Long id, @RequestBody Store store) {
		Store updatedStore = storeService.updateStore(id, store);
		return ResponseEntity.ok(updatedStore);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteStore(@PathVariable Long id) {
		storeService.deleteStore(id);
		return ResponseEntity.ok("Store deleted successfully");
	}
}