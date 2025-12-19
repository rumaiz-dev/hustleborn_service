package com.hustleborn.service.model.productvariants;

import java.time.LocalDateTime;

import com.hustleborn.service.model.products.Products;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_variants")
public class ProductVariants {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Products product;

	@Column(unique = true)
	private String sku;

	@Column(columnDefinition = "TEXT")
	private String variantOptions;

	private Double priceAdjustment;

	private Integer stockQuantity;

	@Enumerated(EnumType.STRING)
	private StockStatus stockStatus;

//	private List<String> imageUrls; 

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getVariantOptions() {
		return variantOptions;
	}

	public void setVariantOptions(String variantOptions) {
		this.variantOptions = variantOptions;
	}

	public Double getPriceAdjustment() {
		return priceAdjustment;
	}

	public void setPriceAdjustment(Double priceAdjustment) {
		this.priceAdjustment = priceAdjustment;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public StockStatus getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(StockStatus stockStatus) {
		this.stockStatus = stockStatus;
	}

//	public List<String> getImageUrls() {
//		return imageUrls;
//	}
//
//	public void setImageUrls(List<String> imageUrls) {
//		this.imageUrls = imageUrls;
//	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}