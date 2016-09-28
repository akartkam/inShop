package com.akartkam.inShop.dao.order;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.order.OrderItem;
import com.akartkam.inShop.domain.product.Sku;

public interface OrderItemDAO extends GenericDAO<OrderItem, UUID> {
	public Map<UUID, Integer> findMapOrderItemIdQuantity(Collection<OrderItem> orderItems);
}
