package com.akartkam.inShop.formbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2629554330145347921L;
	private UUID Id = UUID.randomUUID();
	private List<CartItemForm> cartItems = new ArrayList<CartItemForm>();

	public List<CartItemForm> getCartItems() {
		return cartItems;
	}
	public void setCartItems(List<CartItemForm> cartItems) {
		this.cartItems = cartItems;
	}
	public UUID getId() {
		return Id;
	}
	
    private CartItemForm findItemByCode(UUID code) {
        for (CartItemForm item : getCartItems()) {
            /*if (item.getItemCode().equals(code)) {
                return item;
            }*/
        }
        return null;
    }
	
	public void addCartItem(SkuForm sku, int quantity) {
		CartItemForm item = this.findItemByCode(sku.getId()); 
        if (item == null) {
        	item = new CartItemForm();
        	item.setQuantity(1);
        	//item.setSkuForm(sku);
            getCartItems().add(item);
        }
        int newQuantity = item.getQuantity() + quantity;
        if (newQuantity <= 0) {
            getCartItems().remove(item);
        } else {
            item.setQuantity(newQuantity);
        }
	}
	
	public void removeCartItem(SkuForm sku) {
		CartItemForm item = findItemByCode(sku.getId());
        if (item != null) {
            getCartItems().remove(item);
        }
	}
	
    public int getQuantityTotal() {
        int quantity = 0;
        for (CartItemForm item : getCartItems()) {
            quantity += item.getQuantity();
        }
        return quantity;
    }	
	
    public BigDecimal getTotal() {
    	BigDecimal returnValue = BigDecimal.ZERO;
        for (CartItemForm item : getCartItems()) {
        	//returnValue = returnValue.add(item.getRowTotal());
        }
        return returnValue;
    }	

}
