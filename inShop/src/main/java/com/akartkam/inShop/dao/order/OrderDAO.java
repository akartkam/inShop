package com.akartkam.inShop.dao.order;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.formbean.DataTableForm;


public interface OrderDAO extends GenericDAO<Order, UUID> {
	long countTotalOrders();
	Object[] findOrdersForDataTable(DataTableForm dt);
	List<Object[]> findOrdersByStatus();
}
