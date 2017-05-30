package com.akartkam.inShop.formbean;

import java.io.Serializable;
import java.util.UUID;


public class ProductFilterFacetDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4526974203562147450L;
	private UUID id;
	private String name;
	private boolean active;
	private Integer quantity;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
