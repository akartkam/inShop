package com.akartkam.inShop.formbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CartItemForm implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8806912685052493247L;
	private String productId;
	private String skuId;
	private int quantity;
	private Map<String,String> itemAttributes = new HashMap<String,String>();
	private String productName;

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Map<String, String> getItemAttributes() {
		return itemAttributes;
	}
	public void setItemAttributes(Map<String, String> itemAttributes) {
		this.itemAttributes = itemAttributes;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	

}
