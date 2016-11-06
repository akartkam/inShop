package com.akartkam.inShop.validator;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.order.OrderItem;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.service.order.InventoryService;
import com.akartkam.inShop.service.order.OrderService;
import com.akartkam.inShop.util.CommonUtil;

@Component
public class OrderValidator implements Validator {
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private OrderService orderService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Order.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Order order = (Order) target;
		
		if (order.getOrderItems().isEmpty()) return;
	 	Map<Sku, Integer> mapOfQuants = inventoryService.retrieveQuantitiesAvailable(order.getSkusFromOrderItems());
	 	Map<OrderItem, Integer> mapOfOrderItemQuants = orderService.retrieveOrderItemQuantities(order.getOrderItems());
	 	int oiQuant;
	 	Integer currQuant, currOIQuant;
	 	OrderItem orderItem;
        for (int i = 0; i < order.getOrderItems().size(); i++) {
        	orderItem = order.getOrderItems().get(i);
            oiQuant = CommonUtil.nullSafeIntegerToPrimitive(orderItem.getQuantity());
            currQuant = mapOfQuants.get(orderItem.getSku());
            //if null, then sku is always available
            if (currQuant != null) {
                currOIQuant = mapOfOrderItemQuants.get(orderItem);
                if (currOIQuant == null) currOIQuant = 0;
                currQuant += currOIQuant;
            	if(currQuant < oiQuant) {
	                errors.rejectValue("orderItems[" + i + "].quantity", "error.insufficient.quantity", new String[] {currQuant.toString()}, null);
	            }
            }

        }
		

	}

}
