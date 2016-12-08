package com.akartkam.inShop.formbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

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
    
    public CartItemForm getCartItemForm(CartItemForm cartItem) {
    	for (CartItemForm exCartItem : getCartItems()) {
    		if (exCartItem.equals(cartItem)) {
    			return exCartItem;
    		}
    	} 
    	return null;
    }
    
    
    public int getCartItemFormQuantity(CartItemForm cartItem) {
    	int res = 0;
    	CartItemForm exCartItem = getCartItemForm(cartItem);
    	if (exCartItem != null) res = exCartItem.getQuantity();
    	return res;
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
	
    @NumberFormat(style=Style.CURRENCY)
    public BigDecimal getTotal() {
    	BigDecimal returnValue = BigDecimal.ZERO;
        for (CartItemForm item : getCartItems()) {
        	returnValue = returnValue.add(item.getCartItemTotal());
        }
        return returnValue;
    }	

}
