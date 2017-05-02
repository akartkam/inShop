package com.akartkam.inShop.dao;

import java.util.List;
import java.util.UUID;

public interface ReportsDAO {
	Object[] findOrdersCheck(UUID order_id);
	List<Object[]> findOrderItemsCheck(UUID order_id);
}
