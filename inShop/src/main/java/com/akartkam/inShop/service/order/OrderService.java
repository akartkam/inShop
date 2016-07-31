package com.akartkam.inShop.service.order;

import java.util.UUID;

import com.akartkam.inShop.domain.order.OrderItem;

public interface OrderService {
	OrderItem getOrderItemById(UUID id);
}
