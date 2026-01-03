package com.hustleborn.service.repository.productcategory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hustleborn.service.model.productcategory.ProductCategories;

@Repository
public interface ProductCategoriesRepository extends JpaRepository<ProductCategories, Long> {
    List<ProductCategories> findByParent(Long parent);
}