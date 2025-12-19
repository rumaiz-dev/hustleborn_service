package com.hustleborn.service.repository.productattributes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hustleborn.service.model.productattributes.ProductAttributes;

@Repository
public interface ProductAttributesRepository extends JpaRepository<ProductAttributes, Long> {

    List<ProductAttributes> findByProductId(Long productId);

    List<ProductAttributes> findByAttributeName(String attributeName);

    List<ProductAttributes> findByIsVariant(Boolean isVariant);
}