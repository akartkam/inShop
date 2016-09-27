package com.akartkam.inShop.validator;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.order.OrderItem;
import com.akartkam.inShop.domain.product.Sku;
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
	 	Map<Sku, Integer> mapOfQuants = inventoryService.retrieveQuantitiesAvailable(order.getSkusFromOrderItems());
        for (int i = 0; i < order.getOrderItems().size(); i++) {
            OrderItem orderItem = order.getOrderItems().get(i);
            Integer currQuant = mapOfQuants.get(orderItem.getSku());
            //if null, then sku is always available
            if ( currQuant != null) {
	            if(currQuant < orderItem.getQuantity()) {
	                errors.rejectValue("orderItems[" + i + "].quantity", "error.insufficient.quantity", new String[] {currQuant.toString()}, null);
	            }
            }

        }
		

	}

}
