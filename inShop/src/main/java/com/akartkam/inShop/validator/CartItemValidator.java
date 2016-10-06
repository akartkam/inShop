package com.akartkam.inShop.validator;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.formbean.CartItemForm;
import com.akartkam.inShop.service.order.InventoryService;
import com.akartkam.inShop.service.product.ProductService;

public class CartItemValidator implements Validator {
	
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
		String producrId = cartItem.getProductId();
		if (producrId != null && "".equals(producrId.trim())) {
			try {
				Product product = productService.getProductById(UUID.fromString(producrId));
				if (product == null) {
					error.rejectValue("productId", "error.productNotFound.cartItemForm.productId", new String[] {producrId}, null);
				}
			} catch (IllegalArgumentException e) {
				error.rejectValue("productId", "error.badId.cartItemForm.productId", new String[] {producrId}, null);
			}
		} else {
			error.rejectValue("productId", "error.badId.cartItemForm.productId");
		}
		

	}

}
