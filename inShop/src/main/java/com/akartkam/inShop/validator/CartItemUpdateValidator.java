package com.akartkam.inShop.validator;



import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.exception.InventoryUnavailableException;
import com.akartkam.inShop.exception.SkuNotFoundException;
import com.akartkam.inShop.exception.UpdateCartException;
import com.akartkam.inShop.formbean.CartItemForm;
import com.akartkam.inShop.service.order.InventoryService;
import com.akartkam.inShop.service.product.ProductService;

@Component
public class CartItemUpdateValidator implements Validator {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private InventoryService inventoryService;

	@Override
	public boolean supports(Class<?> arg0) {
		return CartItemForm.class.isAssignableFrom(arg0);
	}

	@Override
	public void validate(Object arg0, Errors error) {
		CartItemForm cartItem = (CartItemForm) arg0;
		Sku sku = null;
		if (cartItem.getQuantity() < 0) {
			error.rejectValue("quantity", "error.bad.cartItemForm.quantity", new String[] {String.valueOf(cartItem.getQuantity())}, null);
		}
		if (cartItem.getProductId() == null || "".equals(cartItem.getProductId())) {
			error.rejectValue("productId", "error.bad.cartItemForm.productId", new String[] {"''"}, null);
		}
		if (cartItem.getSkuId() == null || "".equals(cartItem.getSkuId())) {
			error.rejectValue("productId", "error.bad.cartItemForm.skuId", new String[] {"''"}, null);
		}	
		if (cartItem.getSkuId() != null && !"".equals(cartItem.getSkuId())){
			try {
				sku = productService.getSkuById(UUID.fromString(cartItem.getSkuId()));
			} catch (IllegalArgumentException e) {
				error.rejectValue("skuId", "error.bad.cartItemForm.skuId", new String[] {cartItem.getSkuId()}, null);
			}
		}
		try {
			if (sku != null) {
		        boolean isAvailable = inventoryService.isQuantityAvailable(sku, cartItem.getQuantity());
		        if (!isAvailable) 
		        	throw new InventoryUnavailableException("The referenced Sku " + sku.getId() + " is marked as unavailable, or an insufficient amount",
		        			                                 sku.getId(), cartItem.getQuantity(), inventoryService.retrieveQuantityAvailable(sku));
			} else {
				throw new SkuNotFoundException("The referenced Sku "+cartItem.getSkuId()+" could not found.");			
			}
		
		} catch (SkuNotFoundException | InventoryUnavailableException e) {
			throw new UpdateCartException("Could not update cart", e);
		}		
	}
}
