package com.akartkam.inShop.service.order;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.order.OrderDAO;
import com.akartkam.inShop.dao.order.OrderItemDAO;
import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.order.OrderItem;
import com.akartkam.inShop.util.OrderNumberGenerator;

@Service("OrderService")
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private ApplicationContext appContext;
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private OrderItemDAO orderItemDAO;	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public OrderItem getOrderItemById(UUID id) {
		return orderItemDAO.get(id);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderDAO.list();
	}

	@Override
	@Transactional(readOnly = false)
	public Order createOrder(Order order) {
		if (order.getOrderNumber() == null || "".equals(order.getOrderNumber())) {
			OrderNumberGenerator onGen = (OrderNumberGenerator)appContext.getBean("orderNumberGenerator");
			String on = onGen.generateOrderNumber(jdbcTemplate);
			order.setOrderNumber(on);
		}
		return orderDAO.create(order);
	}

	@Override
	public Order getOrderById(UUID id) {
		return orderDAO.get(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(Order order) {
		if (order == null) return;
		Order existingOrder = getOrderById(order.getId());
		if (existingOrder != null) {
			existingOrder.setCustomer(order.getCustomer());
			existingOrder.setEmailAddress(order.getEmailAddress());
			Iterator<OrderItem> ioi = existingOrder.getOrderItems().iterator();
			while (ioi.hasNext()) {
				OrderItem oi = ioi.next();
				
			}
		} else {
			
		}
		
	}
}
