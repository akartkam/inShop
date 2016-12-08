package com.akartkam.inShop.formbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.akartkam.inShop.domain.product.Sku;

public class CartItemForm implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8806912685052493247L;
	private String productId;
	private String skuId;
	private String imageUrl;
	private Sku sku;
	private int quantity;
	private int fullQuantityOnCart;
	private BigDecimal price;
	private Map<String,String> itemAttributes = new HashMap<String,String>();
	private String productName;

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getFullQuantityOnCart() {
		return fullQuantityOnCart;
	}
	public void setFullQuantityOnCart(int fullQuantity) {
		this.fullQuantityOnCart = fullQuantity;
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
	
	public Sku getSku() {
		return sku;
	}
	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	@NumberFormat(style=Style.CURRENCY)
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public boolean isValid() {
		return (skuId != null && !"".equals(skuId));
	}
	
	@NumberFormat(style=Style.CURRENCY)
	public BigDecimal getCartItemTotal() {
		BigDecimal res = BigDecimal.ZERO;
		if (getPrice() != null) {
			BigDecimal quant = BigDecimal.valueOf(getQuantity());
			res = getPrice().multiply(quant);
		}
		return res;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((skuId == null) ? 0 : skuId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CartItemForm))
		      return false;		
		if (getClass() != obj.getClass())
			return false;
		CartItemForm other = (CartItemForm) obj;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (skuId == null) {
			if (other.skuId != null)
				return false;
		} else if (!skuId.equals(other.skuId))
			return false;
		return true;
	}
	
}
