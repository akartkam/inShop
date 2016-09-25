package com.akartkam.inShop.formbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class CartItemForm implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8806912685052493247L;
	private UUID code;
	private SkuForm skuForm;
	private int quantity;
	public UUID getCode() {
		return code;
	}
	public void setCode(UUID code) {
		this.code = code;
	}
	public SkuForm getSkuForm() {
		return skuForm;
	}
	public void setSkuForm(SkuForm skuForm) {
		this.skuForm = skuForm;
		this.code = skuForm.getId();
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
