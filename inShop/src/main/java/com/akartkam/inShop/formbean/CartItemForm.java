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
	private UUID productId;
	private UUID skuId;
	private int quantity;
	private Map<String,String> itemAttributes = new HashMap<String,String>();

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public UUID getProductId() {
		return productId;
	}
	public void setProductId(UUID productId) {
		this.productId = productId;
	}
	public Map<String, String> getItemAttributes() {
		return itemAttributes;
	}
	public void setItemAttributes(Map<String, String> itemAttributes) {
		this.itemAttributes = itemAttributes;
	}
	public UUID getSkuId() {
		return skuId;
	}
	public void setSkuId(UUID skuId) {
		this.skuId = skuId;
	}
	
	

	

}
