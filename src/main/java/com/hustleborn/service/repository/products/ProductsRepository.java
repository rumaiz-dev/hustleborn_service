package com.hustleborn.service.repository.products;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hustleborn.service.model.products.ProductType;
import com.hustleborn.service.model.products.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    List<Products> findByStoreId(Long storeId);
    List<Products> findByProductType(ProductType productType);
    List<Products> findByParentId(Long parentId);
    List<Products> findByProductTypeAndParentId(ProductType productType, Long parentId);
}
