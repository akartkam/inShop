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
	private List<String> modelFacets = new ArrayList<String>();
	private BigDecimal minPrice = new BigDecimal(0);
	private BigDecimal maxPrice = new BigDecimal(0);
	private Map<ProductFilterFacetDTO, List<ProductFilterFacetDTO>> prodAttrsFacets = new HashMap<ProductFilterFacetDTO, List<ProductFilterFacetDTO>>();
	private Map<ProductFilterFacetDTO, List<ProductFilterFacetDTO>> skuAttrsFacets = new HashMap<ProductFilterFacetDTO, List<ProductFilterFacetDTO>>();

	public List<ProductFilterFacetDTO> getBrandFacets() {
		return brandFacets;
	}
	public void setBrandFacets(List<ProductFilterFacetDTO> brandFacets) {
		this.brandFacets = brandFacets;
	}
	public List<String> getModelFacets() {
		return modelFacets;
	}
	public void setModelFacets(List<String> modelFacets) {
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
	public Map<ProductFilterFacetDTO, List<ProductFilterFacetDTO>> getProdAttrsFacets() {
		return prodAttrsFacets;
	}
	public void setProdAttrsFacets(
			Map<ProductFilterFacetDTO, List<ProductFilterFacetDTO>> prodAttrsFacets) {
		this.prodAttrsFacets = prodAttrsFacets;
	}
	public Map<ProductFilterFacetDTO, List<ProductFilterFacetDTO>> getSkuAttrsFacets() {
		return skuAttrsFacets;
	}
	public void setSkuAttrsFacets(
			Map<ProductFilterFacetDTO, List<ProductFilterFacetDTO>> skuAttrsFacets) {
		this.skuAttrsFacets = skuAttrsFacets;
	}

	

}
