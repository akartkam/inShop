package com.akartkam.inShop.service.order;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.order.OrderItem;

public interface OrderService {
	OrderItem getOrderItemById(UUID id);
	List<Order> getAllOrders();
	Order createOrder(Order order);
	Order getOrderById(UUID id);
	void mergeWithExistingAndUpdateOrCreate(Order order);
	Map<OrderItem, Integer> retrieveOrderItemQuantities(Collection<OrderItem> orderItems);
}
