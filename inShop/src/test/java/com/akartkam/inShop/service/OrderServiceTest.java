package com.akartkam.inShop.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.akartkam.inShop.common.AbstractTest;
import com.akartkam.inShop.domain.customer.Customer;
import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.order.OrderItem;
import com.akartkam.inShop.domain.order.OrderStatus;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.service.customer.CustomerService;
import com.akartkam.inShop.service.order.OrderService;
import com.akartkam.inShop.service.product.ProductService;


public class OrderServiceTest extends AbstractTest {
	private static final Log LOG = LogFactory.getLog(OrderServiceTest.class);
	
	@Autowired
	private OrderService orderService;	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductService productService;
	@Autowired
	private SessionFactory sessionFactory;	
	
	
	@Test
	public void addOrderTest() {
		Order or = new Order();
		Customer cs = new Customer();
		cs.setFirstName("Test_FirstName");
		cs.setLastName("Test_LastName");
		cs.setEmail("qwer@qwer.com");
		or.setCustomer(cs);
		or.setEmailAddress(cs.getEmail());
		or.setSubmitDate(new DateTime());
		or.setStatus(OrderStatus.NEW);
		OrderItem oi = new OrderItem();
		Sku sku = productService.getSkuById(UUID.fromString("4ea061cf-dbaf-485b-873d-c92924e3c4f2"));
		oi.setSku(sku);
		oi.setQuantity(1);
		oi.setProduct(sku.getDefaultProduct());
		oi.setPrice(sku.getRetailPrice());
		oi.setRetailPrice(sku.getRetailPrice());
		oi.setSalePrice(sku.getSalePrice());
		or.addOrderItem(oi);
		UUID orId = or.getId();
		orderService.createOrder(or);
		sessionFactory.getCurrentSession().flush();
		Order or1 = orderService.getOrderById(orId);
		assertNotNull(or1);
		assertEquals(or, or1);
		assertNotNull(or1.getOrderNumber());
		logger.info(or1.getOrderNumber());
	}

}
