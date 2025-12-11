package com.hustleborn.service.model.products;

import java.util.List;

public class ProductAttributes {

	private String slug;

	private List<String> options;

	private Integer position;

	private boolean visible;

	private boolean variation;

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVariation() {
		return variation;
	}

	public void setVariation(boolean variation) {
		this.variation = variation;
	}

}
