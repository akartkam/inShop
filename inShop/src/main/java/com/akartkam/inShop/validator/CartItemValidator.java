package com.akartkam.inShop.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
	public void validate(Object arg0, Errors arg1) {
		CartItemForm cartItem = (CartItemForm) arg0;
		

	}

}
