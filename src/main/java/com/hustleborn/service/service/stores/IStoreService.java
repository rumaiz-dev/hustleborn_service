package com.hustleborn.service.service.stores;

import java.util.List;

import com.hustleborn.service.model.stores.Store;

public interface IStoreService {
    List<Store> getAllStores();
    Store getStoreById(Long id);
    Store createStore(Store store);
    Store updateStore(Long id, Store store);
    void deleteStore(Long id);
}