package com.akartkam.inShop.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.service.order.InventoryService;

public class OrderValidator implements Validator {
	
	@Autowired
	private InventoryService inventoryService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Order.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Order order = (Order) target;

	}

}
