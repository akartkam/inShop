package com.akartkam.inShop.formbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProductFilterDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1609658387265588577L;
	private List<ProductFilterFacetDTO> brandFacets = new ArrayList<ProductFilterFacetDTO>();
	private List<ProductFilterFacetDTO> modelFacets = new ArrayList<ProductFilterFacetDTO>();
	private BigDecimal minPrice = new BigDecimal(0);
	private BigDecimal maxPrice = new BigDecimal(0);
	private List<ProductFilterFacetDTO> attributesFacets = new ArrayList<ProductFilterFacetDTO>();
	private UUID categoryId;
	private UUID brandId;
	
	public List<ProductFilterFacetDTO> getBrandFacets() {
		return brandFacets;
	}
	public void setBrandFacets(List<ProductFilterFacetDTO> brandFacets) {
		this.brandFacets = brandFacets;
	}
	public List<ProductFilterFacetDTO> getModelFacets() {
		return modelFacets;
	}
	public void setModelFacets(List<ProductFilterFacetDTO> modelFacets) {
		this.modelFacets = modelFacets;
	}
	public BigDecimal getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}
	public BigDecimal getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}
	public UUID getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(UUID categoryId) {
		this.categoryId = categoryId;
	}
	public UUID getBrandId() {
		return brandId;
	}
	public void setBrandId(UUID brandId) {
		this.brandId = brandId;
	}
	public List<ProductFilterFacetDTO> getAttributesFacets() {
		return attributesFacets;
	}
	public void setAttributesFacets(List<ProductFilterFacetDTO> attributesFacets) {
		this.attributesFacets = attributesFacets;
	}

	

}
