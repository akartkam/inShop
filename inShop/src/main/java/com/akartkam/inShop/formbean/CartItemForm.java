package com.akartkam.inShop.formbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class CartItemForm implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8806912685052493247L;
	private UUID itemCode;
	private SkuForm sku;
	private int quantity;
	public UUID getItemCode() {
		return itemCode;
	}
	public void setItemCode(UUID code) {
		this.itemCode = code;
	}
	public SkuForm getSkuForm() {
		return sku;
	}
	public void setSkuForm(SkuForm sku) {
		this.sku = sku;
		this.itemCode = sku.getId();
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getRowTotal(){
    	BigDecimal returnValue = BigDecimal.ZERO;
    	BigDecimal quant = BigDecimal.valueOf(quantity);
        if (getSkuForm().getSalePrice() != null) {
            returnValue = getSkuForm().getSalePrice().multiply(quant);
        } 
        return returnValue;		
	}
}
