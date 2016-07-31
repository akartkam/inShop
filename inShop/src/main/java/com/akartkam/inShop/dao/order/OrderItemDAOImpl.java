package com.akartkam.inShop.dao.order;

import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.order.OrderItem;

@Repository
public class OrderItemDAOImpl extends AbstractGenericDAO<OrderItem> implements OrderItemDAO {

}
