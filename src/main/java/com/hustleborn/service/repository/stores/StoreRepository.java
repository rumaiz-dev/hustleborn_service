package com.hustleborn.service.repository.stores;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hustleborn.service.model.stores.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    // Add custom query methods if needed
}