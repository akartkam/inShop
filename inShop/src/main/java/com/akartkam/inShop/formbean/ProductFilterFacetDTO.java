package com.akartkam.inShop.formbean;

import java.io.Serializable;

public class ProductFilterFacetDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4526974203562147450L;
	private String facet;
	private String id;
	private boolean active;
	private Integer quantity;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Override
    public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((facet == null) ? 0 : facet.hashCode());
		return result;		
    }
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof ProductFilterFacetDTO))
		      return false;		
		if (getClass() != o.getClass())
			return false;		
		ProductFilterFacetDTO other = (ProductFilterFacetDTO) o;
		if (id == null) {
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (facet == null) {
			if (other.facet != null)
				return false;
		} else if (!facet.equals(other.facet))
			return false;
		return true;
    }
	
	public String getFacet() {
		return facet;
	}
	public void setFacet(String facet) {
		this.facet = facet;
	}	
	
}
