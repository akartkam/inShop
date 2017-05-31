package com.akartkam.inShop.formbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductFilterDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1609658387265588577L;
	private List<ProductFilterFacetDTO> brandFacets = new ArrayList<ProductFilterFacetDTO>();
	private List<ProductFilterFacetDTO> modelFacets = new ArrayList<ProductFilterFacetDTO>();
	private BigDecimal minPrice = new BigDecimal(0);
	private BigDecimal maxPrice = new BigDecimal(0);
	private Map<String, List<ProductFilterFacetDTO>> attributesFacets = new HashMap<String, List<ProductFilterFacetDTO>>();
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
	public Map<String, List<ProductFilterFacetDTO>> getAttributesFacets() {
		return attributesFacets;
	}
	public void setAttributesFacets(
			Map<String, List<ProductFilterFacetDTO>> attributesFacets) {
		this.attributesFacets = attributesFacets;
	}

	

}
