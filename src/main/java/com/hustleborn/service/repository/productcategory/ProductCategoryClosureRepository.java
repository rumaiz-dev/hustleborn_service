package com.hustleborn.service.repository.productcategory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hustleborn.service.model.productcategory.ProductCategoryClosure;

@Repository
public interface ProductCategoryClosureRepository extends JpaRepository<ProductCategoryClosure, Long> {

    // Find all descendants of a category (including itself)
    @Query("SELECT c FROM ProductCategoryClosure c WHERE c.ancestorId = :categoryId")
    List<ProductCategoryClosure> findDescendants(@Param("categoryId") Long categoryId);

    // Find all ancestors of a category (including itself)
    @Query("SELECT c FROM ProductCategoryClosure c WHERE c.descendantId = :categoryId")
    List<ProductCategoryClosure> findAncestors(@Param("categoryId") Long categoryId);

    // Find direct children (depth = 1)
    @Query("SELECT c FROM ProductCategoryClosure c WHERE c.ancestorId = :categoryId AND c.depth = 1")
    List<ProductCategoryClosure> findDirectChildren(@Param("categoryId") Long categoryId);

    // Delete all entries for a category (used when deleting a category)
    @Modifying
    @Query("DELETE FROM ProductCategoryClosure c WHERE c.ancestorId = :categoryId OR c.descendantId = :categoryId")
    void deleteByCategoryId(@Param("categoryId") Long categoryId);

    // Find descendants excluding self
    @Query("SELECT c FROM ProductCategoryClosure c WHERE c.ancestorId = :categoryId AND c.depth > 0")
    List<ProductCategoryClosure> findDescendantsExcludingSelf(@Param("categoryId") Long categoryId);

    @Modifying
    @Query("DELETE FROM ProductCategoryClosure c WHERE c.ancestorId IN :ids OR c.descendantId IN :ids")
    void deleteByCategoryIds(@Param("ids") List<Long> ids);

}