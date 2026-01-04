package com.hustleborn.service.service.stores;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustleborn.service.model.stores.Store;
import com.hustleborn.service.repository.stores.StoreRepository;
import com.hustleborn.service.utils.exceptions.ApiException;

@Service
public class StoreService {

	@Autowired
	private StoreRepository storeRepository;

	public List<Store> getAllStores() {
		return storeRepository.findAll();
	}

	public Store getStoreById(Long id) {
		return storeRepository.findById(id).orElseThrow(() -> new ApiException("Store not found", id, null));
	}

	public Store createStore(Store store) {
		store.setCreatedAt(LocalDateTime.now());
		store.setUpdatedAt(LocalDateTime.now());
		return storeRepository.save(store);
	}

	public Store updateStore(Long id, Store storeDetails) {
		Store store = getStoreById(id);
		store.setName(storeDetails.getName());
		store.setAddress(storeDetails.getAddress());
		store.setPhone(storeDetails.getPhone());
		store.setEmail(storeDetails.getEmail());
		store.setIsActive(storeDetails.getIsActive());
		store.setUpdatedAt(LocalDateTime.now());
		return storeRepository.save(store);
	}

	public void deleteStore(Long id) {
		Store store = getStoreById(id);
		storeRepository.delete(store);
	}
}