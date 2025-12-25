package com.hustleborn.service.model.billitems;

import com.hustleborn.service.model.bills.Bills;
import com.hustleborn.service.model.productvariants.ProductVariants;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bill_items")
public class BillItems {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    private Bills bill;

    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariants variant;

    private Integer quantity;

    private Double unitPrice;

    private Double discountAmount;

    private Double totalPrice;

    private Integer refundedQuantity;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bills getBill() {
        return bill;
    }

    public void setBill(Bills bill) {
        this.bill = bill;
    }

    public ProductVariants getVariant() {
        return variant;
    }

    public void setVariant(ProductVariants variant) {
        this.variant = variant;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getRefundedQuantity() {
        return refundedQuantity;
    }

    public void setRefundedQuantity(Integer refundedQuantity) {
        this.refundedQuantity = refundedQuantity;
    }
}