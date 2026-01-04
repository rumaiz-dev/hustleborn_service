package com.hustleborn.service.model.products;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.hustleborn.service.model.productattributes.ProductAttributes;
import com.hustleborn.service.model.productcategory.ProductCategories;
import com.hustleborn.service.converter.StringMapConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Products {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@Column(unique = true)
	private String slug;

	private String sku;

	private Double price;

	private Double purchasingPrice;

	private Double salePrice;

	private String description;

//	private List<String> imageUrls;

	@Column(nullable = false)
	private Long accountId;

	private Long storeId;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "product_product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "product_category_id"))
	private List<ProductCategories> productCategories;


	@Column(unique = true)
	private String code;

	private int stockQuantity;

	private String stockStatus;

	@Enumerated(EnumType.STRING)
	private ProductStatus status;

	@Enumerated(EnumType.STRING)
	private ProductType productType;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private Long parentId;

	private String variant;

	private String linkAddress;

	@Convert(converter = StringMapConverter.class)
	@Column(columnDefinition = "TEXT")
	private Map<String, ProductAttributes> attributes;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "json")
	private Map<String, Object> dimensions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getPurchasingPrice() {
		return purchasingPrice;
	}

	public void setPurchasingPrice(Double purchasingPrice) {
		this.purchasingPrice = purchasingPrice;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	public List<String> getImageUrls() {
//		return imageUrls;
//	}
//
//	public void setImageUrls(List<String> imageUrls) {
//		this.imageUrls = imageUrls;
//	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public List<ProductCategories> getProductCategories() {
		return productCategories;
	}

	public void setProductCategories(List<ProductCategories> productCategories) {
		this.productCategories = productCategories;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public String getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}

	public ProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProductStatus status) {
		this.status = status;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public Map<String, Object> getDimensions() {
		return dimensions;
	}

	public void setDimensions(Map<String, Object> dimensions) {
		this.dimensions = dimensions;
	}

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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public String getLinkAddress() {
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	public Map<String, ProductAttributes> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, ProductAttributes> attributes) {
		this.attributes = attributes;
	}



}
