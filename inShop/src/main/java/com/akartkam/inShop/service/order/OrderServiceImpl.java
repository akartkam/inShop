package com.akartkam.inShop.service.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.order.OrderDAO;
import com.akartkam.inShop.dao.order.OrderItemDAO;
import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.order.OrderItem;
import com.akartkam.inShop.domain.product.Product;
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
			existingOrder.setSubmitDate(order.getSubmitDate());
			existingOrder.setStatus(order.getStatus());
			List<OrderItem> loif = new ArrayList<OrderItem>(order.getOrderItems());
			Iterator<OrderItem> ioi = existingOrder.getOrderItems().iterator();
			while (ioi.hasNext()) {
				OrderItem oi = ioi.next();
				int idx = loif.indexOf(oi);
				if (idx != -1) {
					OrderItem oif = loif.get(idx);
					oi.setPrice(new BigDecimal(oif.getPrice().toPlainString()));
					oi.setQuantity(oif.getQuantity());
					loif.remove(idx);
				} else {
				    ioi.remove();
				}
			}
			for(OrderItem oi1: loif) {
				Product p = oi1.getSku().getDefaultProduct() != null ? oi1.getSku().getDefaultProduct() : oi1.getSku().getProduct();
				oi1.setProduct(p);
				oi1.setCategory(p.getCategory());
				oi1.setRetailPrice(oi1.getSku().getRetailPrice());
				oi1.setSalePrice(oi1.getSku().getSalePrice());
				existingOrder.addOrderItem(oi1); 
			}
			if (existingOrder.calculateSubTotal() != existingOrder.getSubTotal()) existingOrder.setSubTotal(existingOrder.calculateSubTotal());
			if (existingOrder.calculateTotal() != existingOrder.getTotal()) existingOrder.setTotal(existingOrder.calculateTotal());
		} else {
			for (OrderItem oi : order.getOrderItems()) {
				Product p = oi.getSku().getDefaultProduct() != null ? oi.getSku().getDefaultProduct() : oi.getSku().getProduct();
				oi.setProduct(p);
				oi.setCategory(p.getCategory());
				oi.setRetailPrice(oi.getSku().getRetailPrice());
				oi.setSalePrice(oi.getSku().getSalePrice());
				oi.setOrder(order);
			}
			if (order.calculateSubTotal() != order.getSubTotal()) order.setSubTotal(order.calculateSubTotal());
			if (order.calculateTotal() != order.getTotal()) order.setTotal(order.calculateTotal());
			createOrder(order);
		}
		
	}
	
	@Override
	public Map<OrderItem, Integer> retrieveOrderItemQuantities(Collection<OrderItem> orderItems) {
		Map<OrderItem, Integer> quantities = new HashMap<OrderItem, Integer>();
        Map<UUID, Integer> orderItemsMap = orderItemDAO.findMapOrderItemIdQuantity(orderItems);
        for (OrderItem orderItem : orderItems) {
        	quantities.put(orderItem, orderItemsMap.get(orderItem.getId()));
        }
        return quantities;
	}
	
}
