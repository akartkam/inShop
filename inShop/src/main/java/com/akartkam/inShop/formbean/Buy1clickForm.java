package com.akartkam.inShop.formbean;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.domain.product.Sku;

public class Buy1clickForm implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7030332322089410053L;
	private Sku sku;
	private String name, phone;
	
	public Sku getSku() {
		return sku;
	}
	public void setSku(Sku sku) {
		this.sku = sku;
	}
	@NotEmpty
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@NotEmpty
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
}
