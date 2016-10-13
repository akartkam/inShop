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
	
    
    public int getCartItemsCount() {
    	return getCartItems().size();
    }
	
	public boolean addCartItem(CartItemForm cartItem) {
        if (cartItem == null) return false;
        int index = getCartItems().indexOf(cartItem);
        if (index >= 0) {
        	CartItemForm exCartItem = getCartItems().get(index);
        	exCartItem.setQuantity(exCartItem.getQuantity()+cartItem.getQuantity());
        	return true;
        }
        return getCartItems().add(cartItem);
 	}
	
	public boolean removeCartItem(CartItemForm cartItem) {
		if (cartItem == null) return false;
		return getCartItems().remove(cartItem);
	}
	
	public boolean updateQuantityOnCartItem(CartItemForm cartItem) {
		if (cartItem == null) return false;
	    int index = getCartItems().indexOf(cartItem);
	    if (index >= 0) {
	    	CartItemForm exCartItem = getCartItems().get(index);
	    	exCartItem.setQuantity(cartItem.getQuantity());
	    	return true;
	    }
	    return false;
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
        	returnValue = returnValue.add(item.getCartItemTotal());
        }
        return returnValue;
    }	

}
