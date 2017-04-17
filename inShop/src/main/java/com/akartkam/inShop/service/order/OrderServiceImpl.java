package com.akartkam.inShop.service.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.controller.DefaultErrorController;
import com.akartkam.inShop.dao.order.OrderDAO;
import com.akartkam.inShop.dao.order.OrderItemDAO;
import com.akartkam.inShop.domain.customer.Customer;
import com.akartkam.inShop.domain.order.Fulfillment;
import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.order.OrderItem;
import com.akartkam.inShop.domain.order.OrderStatus;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.exception.InventoryUnavailableException;
import com.akartkam.inShop.exception.PlaceOrderException;
import com.akartkam.inShop.formbean.Buy1clickForm;
import com.akartkam.inShop.formbean.CartForm;
import com.akartkam.inShop.formbean.CartItemForm;
import com.akartkam.inShop.formbean.CheckoutForm;
import com.akartkam.inShop.formbean.DataTableForm;
import com.akartkam.inShop.formbean.OrderForm;
import com.akartkam.inShop.service.customer.CustomerService;
import com.akartkam.inShop.util.OrderNumberGenerator;

@Service("OrderService")
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService{
	private static final Log LOG = LogFactory.getLog(OrderServiceImpl.class);
	
	@Autowired
	private ApplicationContext appContext;
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private OrderItemDAO orderItemDAO;	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private CustomerService customerService;
	

	
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
	public void mergeWithExistingAndUpdateOrCreate(OrderForm orderForm) throws InventoryUnavailableException {
		//This maps for adjusting the skus quantity
		LOG.info("Saving order...");
		Map<Sku, Integer> incrMapQuant = new HashMap<Sku, Integer>();
		Map<Sku, Integer> decrMapQuant = new HashMap<Sku, Integer>();
		if (orderForm == null) return ;
		Order existingOrder = getOrderById(orderForm.getId());
		if (existingOrder != null) {
			existingOrder.setCustomer(orderForm.getCustomer());
			existingOrder.setEmailAddress(orderForm.getEmailAddress());
			existingOrder.setSubmitDate(orderForm.getSubmitDate());
			existingOrder.setStatus(orderForm.getStatus());
			List<OrderItem> loif = new ArrayList<OrderItem>(orderForm.getOrderItems());
			Iterator<OrderItem> ioi = existingOrder.getOrderItems().iterator();
			while (ioi.hasNext()) {
				OrderItem oi = ioi.next();
				int idx = loif.indexOf(oi);
				if (idx != -1) {
					OrderItem oif = loif.get(idx);
					oi.setPrice(new BigDecimal(oif.getPrice().toPlainString()));
					int diffQuant = oif.getQuantity() - oi.getQuantity();
					if (diffQuant != 0) {
						if (diffQuant < 0) incrMapQuant.put(oi.getSku(), Integer.valueOf(Math.abs(diffQuant)));
						else if (diffQuant > 0) decrMapQuant.put(oi.getSku(), Integer.valueOf(diffQuant));
						oi.setQuantity(oif.getQuantity());						
					}
					loif.remove(idx);
				} else {
					incrMapQuant.put(oi.getSku(), oi.getQuantity());
				    ioi.remove();
				}
			}
			for(OrderItem oi1: loif) {
				Product p = oi1.getSku().lookupProduct();
				oi1.setProduct(p);
				oi1.setCategory(p.getCategory());
				oi1.setRetailPrice(oi1.getSku().getRetailPrice());
				oi1.setSalePrice(oi1.getSku().getSalePrice());
				oi1.setQuantityPerPackage(oi1.getSku().getQuantityPerPackage());
				existingOrder.addOrderItem(oi1); 
				decrMapQuant.put(oi1.getSku(), oi1.getQuantity());
			}
			if (existingOrder.calculateDelivaryTotal() != existingOrder.getDeliveryTotal()) existingOrder.setDeliveryTotal(existingOrder.calculateDelivaryTotal()); 
			if (existingOrder.calculateSubTotal() != existingOrder.getSubTotal()) existingOrder.setSubTotal(existingOrder.calculateSubTotal());
			if (existingOrder.calculateTotal() != existingOrder.getTotal()) existingOrder.setTotal(existingOrder.calculateTotal());
			if (incrMapQuant.size() > 0) inventoryService.incrementInventory(incrMapQuant);
			if (decrMapQuant.size() > 0) inventoryService.decrementInventory(decrMapQuant);
		} else {
			for (OrderItem oi : orderForm.getOrderItems()) {
				Product p = oi.getSku().getDefaultProduct() != null ? oi.getSku().getDefaultProduct() : oi.getSku().getProduct();
				oi.setProduct(p);
				oi.setCategory(p.getCategory());
				oi.setRetailPrice(oi.getSku().getRetailPrice());
				oi.setSalePrice(oi.getSku().getSalePrice());
				oi.setOrder(orderForm);
				decrMapQuant.put(oi.getSku(), oi.getQuantity());
			}
			if (orderForm.calculateDelivaryTotal() != orderForm.getDeliveryTotal()) orderForm.setDeliveryTotal(orderForm.calculateDelivaryTotal());
			if (orderForm.calculateSubTotal() != orderForm.getSubTotal()) orderForm.setSubTotal(orderForm.calculateSubTotal());
			if (orderForm.calculateTotal() != orderForm.getTotal()) orderForm.setTotal(orderForm.calculateTotal());
			createOrder(orderForm);
			if (decrMapQuant.size() > 0) inventoryService.decrementInventory(decrMapQuant);
		}
		LOG.info("Save order complite (id="+orderForm.getId().toString()+")");
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

	@Override
	@Transactional(readOnly = false)
	public Order placeOrder(CheckoutForm checkoutForm, CartForm cartForm) {
		LOG.info("Placing order...");
		try {
			if (checkoutForm == null)
				 throw new IllegalArgumentException("checkoutForm is null");
			if (cartForm == null)
				 throw new IllegalArgumentException("cartForm is null");
			
			Customer customer = customerService.getCustomer(null, checkoutForm);
			if (customer == null) {
				customer = new Customer();
				customer.setFirstName(checkoutForm.getFirstName());
				customer.setLastName(checkoutForm.getLastName());
				customer.setMiddleName(checkoutForm.getMiddleName());
				customer.setCity(checkoutForm.getCity());
				customer.setAddress(checkoutForm.getAddress());
				customer.setEmail(checkoutForm.getEmail());
				customer.setPhone(checkoutForm.getPhone());
				customerService.createCustomer(customer);				
			}
			
			
			Fulfillment fulfil = new Fulfillment();
			fulfil.setDelivery(checkoutForm.getDelivery());
			fulfil.setAddress(checkoutForm.getAddress());
			fulfil.setFirstName(checkoutForm.getFirstName());
			fulfil.setLastName(checkoutForm.getLastName());
			fulfil.setMiddleName(checkoutForm.getMiddleName());
			fulfil.setStore(checkoutForm.getStore());

			Order order = new Order();
			List<OrderItem> ois = new ArrayList<OrderItem>();
			for (CartItemForm ci : cartForm.getCartItems()) {
				OrderItem oi = new OrderItem();
				oi.setSku(ci.getSku());
				Product p = ci.getSku().lookupProduct();
				oi.setProduct(p);
				oi.setCategory(p.getCategory());
				oi.setQuantity(ci.getQuantity());
				oi.setPrice(ci.getPrice());
				oi.setRetailPrice(ci.getSku().getRetailPrice());
				oi.setSalePrice(ci.getSku().getSalePrice());
				oi.setQuantityPerPackage(ci.getSku().getQuantityPerPackage());
				ois.add(oi);
				order.addOrderItem(oi);
			}
			
			order.setEmailAddress(checkoutForm.getEmail());
			order.setSubmitDate(new Date());
			order.addFulfillment(fulfil);
			order.setCustomer(customer);
			order.setDeliveryTotal(order.calculateDelivaryTotal());
			order.setSubTotal(order.calculateSubTotal());
			order.setTotal(order.calculateTotal());
			order = createOrder(order);
			LOG.info("Place order complite (id="+order.getId().toString()+")");
			return order;
		} catch (Exception e) {
			throw new PlaceOrderException("Could not place order", e);
		}
		
	}

	@Override
	@Transactional(readOnly = false)
	public Order placeBuy1click(Buy1clickForm buy1clickForm) {
		LOG.info("Placing buy1click...");
		try {
			if (buy1clickForm == null)
				 throw new IllegalArgumentException("buy1clickForm is null");
			Fulfillment fulfil = new Fulfillment();
			fulfil.setBuy1clickName(buy1clickForm.getName());
			fulfil.setPhone(buy1clickForm.getPhone());
			Order order = new Order();
			OrderItem oi = new OrderItem();
			oi.setSku(buy1clickForm.getSku());
			Product p = buy1clickForm.getSku().lookupProduct();
			oi.setProduct(p);
			oi.setCategory(p.getCategory());
			oi.setQuantity(1);
			oi.setPrice(buy1clickForm.getSku().getPriceForPackage());
			oi.setRetailPrice(buy1clickForm.getSku().getRetailPrice());
			oi.setSalePrice(buy1clickForm.getSku().getSalePrice());
			oi.setQuantityPerPackage(buy1clickForm.getSku().getQuantityPerPackage());
			order.addOrderItem(oi);
			
			order.setStatus(OrderStatus.INCOMPLETE);
			order.setSubmitDate(new Date());
			order.addFulfillment(fulfil);
			order.setDeliveryTotal(order.calculateDelivaryTotal());
			order.setSubTotal(order.calculateSubTotal());
			order.setTotal(order.calculateTotal());
			order = createOrder(order);
			LOG.info("Place order complite (id="+order.getId().toString()+")");
			return order;
		} catch (Exception e) {
			throw new PlaceOrderException("Could not place order", e);
		}
	}

	
	@Override
	public void reattache(Order order) {
		orderDAO.reattach(order);
		
	}

	@Override
	public void refresh(Order order) {
		orderDAO.refresh(order);
	}

	@Override
	public Object[] getProductsForDataTable(DataTableForm dt) {
		return orderDAO.findOrdersForDataTable(dt, "");
	}

	@Override
	public Object[] getProductsForDataTable(DataTableForm dt, String orderStatus) {
		return orderDAO.findOrdersForDataTable(dt, orderStatus);
	}	
	
	@Override
	public long countTotalOrders() {
		return orderDAO.countTotalOrders();
	}

	@Override
	public List<Object[]> getOrdersByStatus() {
		return orderDAO.findOrdersByStatus();
	}


}
