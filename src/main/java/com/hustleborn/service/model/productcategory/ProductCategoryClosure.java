package com.hustleborn.service.model.productcategory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "product_category_closure")
public class ProductCategoryClosure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ancestor_id", nullable = false)
    private Long ancestorId;

    @Column(name = "descendant_id", nullable = false)
    private Long descendantId;

    @Column(nullable = false)
    private Integer depth;

    // Constructors
    public ProductCategoryClosure() {}

    public ProductCategoryClosure(Long ancestorId, Long descendantId, Integer depth) {
        this.ancestorId = ancestorId;
        this.descendantId = descendantId;
        this.depth = depth;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAncestorId() {
        return ancestorId;
    }

    public void setAncestorId(Long ancestorId) {
        this.ancestorId = ancestorId;
    }

    public Long getDescendantId() {
        return descendantId;
    }

    public void setDescendantId(Long descendantId) {
        this.descendantId = descendantId;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategoryClosure that = (ProductCategoryClosure) o;
        return ancestorId.equals(that.ancestorId) && descendantId.equals(that.descendantId);
    }

    @Override
    public int hashCode() {
        return ancestorId.hashCode() * 31 + descendantId.hashCode();
    }
}