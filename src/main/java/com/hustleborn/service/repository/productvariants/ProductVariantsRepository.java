package com.hustleborn.service.repository.productvariants;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hustleborn.service.model.productvariants.ProductVariants;
import com.hustleborn.service.model.productvariants.StockStatus;

@Repository
public interface ProductVariantsRepository extends JpaRepository<ProductVariants, Long> {

    List<ProductVariants> findByProductId(Long productId);

    List<ProductVariants> findByStockStatus(StockStatus stockStatus);

    List<ProductVariants> findBySku(String sku);

    List<ProductVariants> findByStoreId(Long storeId);
}