package com.akartkam.inShop.formbean;

import java.io.Serializable;

public class ProductFilterFacetDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4526974203562147450L;
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
        if (id != null) {
            return id.hashCode();
        } else {
            return super.hashCode();
        }
    }
	
	@Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (id == null) return false;
		if (!(o instanceof ProductFilterFacetDTO))
		      return false;        
		ProductFilterFacetDTO other = (ProductFilterFacetDTO) o;
        return id.equals(other.getId());
    }	
	
}
