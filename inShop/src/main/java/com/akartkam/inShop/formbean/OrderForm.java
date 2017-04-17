package com.akartkam.inShop.formbean;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.akartkam.inShop.domain.order.Fulfillment;
import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.order.OrderItem;

public class OrderForm extends Order {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8533214732086331952L;
	private Fulfillment actualFormFulfillment;
	
	public OrderForm(){};
	
	public OrderForm(Order order) {
		if (order != null) {
			this.setId(order.getId());
			this.setActualFormFulfillment(order.getActualFulfillment());
			this.setCreatedDate(order.getCreatedDate());
			this.setCustomer(order.getCustomer());
			this.setFulfillment(order.getFulfillment());
			this.setEmailAddress(order.getEmailAddress());
			this.setEnabled(order.isEnabled());
			this.setName(order.getName());
			this.setOrdering(order.getOrdering());
			this.setOrderItems(new ArrayList<OrderItem>(order.getOrderItems()));
			this.setOrderNumber(order.getOrderNumber());
			this.setStatus(order.getStatus());
			this.setSubmitDate(order.getSubmitDate());
			this.setDeliveryTotal(order.getDeliveryTotal());
			this.setSubTotal(order.getSubTotal());
			this.setTotal(order.getTotal());
		}
	}
	
	@Override
	public BigDecimal calculateDelivaryTotal() {
    	BigDecimal res = BigDecimal.ZERO;
    	if (actualFormFulfillment.isEnabled() && actualFormFulfillment.getDeliveryPrice() != null){
    	 	BigDecimal cur = actualFormFulfillment.getDeliveryPrice();
  		    res = res.add(cur);
    	}
    	return res;		
	}
	
	
	
	public Fulfillment getActualFormFulfillment() {
		return actualFormFulfillment;
	}
	public void setActualFormFulfillment(Fulfillment actualFormFulfillment) {
		this.actualFormFulfillment = actualFormFulfillment;
	}
	
}
