package com.akartkam.inShop.controller.admin.order;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.format.number.AbstractNumberFormatter;
import org.springframework.http.HttpStatus;

import com.akartkam.inShop.domain.customer.Customer;
import com.akartkam.inShop.domain.order.Delivery;
import com.akartkam.inShop.domain.order.Fulfillment;
import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.order.OrderItem;
import com.akartkam.inShop.domain.order.OrderStatus;
import com.akartkam.inShop.domain.order.Store;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.exception.InventoryUnavailableException;
import com.akartkam.inShop.exception.SkuNotFoundException;
import com.akartkam.inShop.formbean.ItemsForJSON;
import com.akartkam.inShop.formbean.OrderForm;
import com.akartkam.inShop.formbean.SkuForJSON;
import com.akartkam.inShop.service.customer.CustomerService;
import com.akartkam.inShop.service.extension.ProductDisplayNameModificator;
import com.akartkam.inShop.service.order.DeliveryService;
import com.akartkam.inShop.service.order.OrderService;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.validator.OrderValidator;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {
	  private static final Log LOG = LogFactory.getLog(AdminOrderController.class);
	  

	  @Autowired
	  private ProductService productService;

	  @Autowired
	  private OrderService orderService;
	  
	  @Autowired
	  private CustomerService customerService; 
	  
	  @Autowired
	  private OrderValidator orderValidator;
	  
	  @Autowired
	  private DeliveryService deliveryService;	  
	  
	  @Autowired
	  private AbstractNumberFormatter currencyNumberFormatter;
	  
	  @Autowired
	  private ProductDisplayNameModificator productDisplayNameModificator;
		
	  @ModelAttribute("allDeliverys")
	  public List<Delivery> getAllDeliverys() {
		      return deliveryService.getAllDeliverys();
	  }
	  
	  @ModelAttribute("allStores")
	  public List<Store> getAllStores() {
		      return deliveryService.getAllStores();
	  }
	  
	  @ModelAttribute("ordersByStatus")
	  public List<Object[]> getOrdersByStatus(){
		  List<Object[]> res = orderService.getOrdersByStatus();
		  for (int i=0; i < res.size() ;i++){
			  BigDecimal o2 = (BigDecimal)res.get(i)[2];
			  BigDecimal o3 = (BigDecimal)res.get(i)[3];
			  if (o2 != null) res.get(i)[2] = currencyNumberFormatter.print(o2, Locale.getDefault());
			  if (o3 != null) res.get(i)[3] = currencyNumberFormatter.print(o3, Locale.getDefault());
		  }
		  return res;
	  }

	  @ModelAttribute("allCustomers")
	  public List<Customer> getAllCustomers() {
		  return customerService.getAllCustomer();
	  }
	  
	  @ModelAttribute("allOrderStatus")
	  public List<OrderStatus> getAllOrderStatus() {
	      return Arrays.asList(OrderStatus.ALL);
	  }		  
	  
	  
	  @Autowired
	  private MessageSource messageSource;
	  
	  
	  @InitBinder()
	  public void initBinder(WebDataBinder binder) {
		    binder.setAllowedFields(new String[] {"*id", "customer", "status", "submitDate", "orderNumber", "emailAddress", 
		    		                              "orderItems*", "createdDate", "actualFormFulfillment", "fulfillment", 
		    		                              "*fulfillment*", "actualFormFulfillment*"});
		    binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });
			binder.registerCustomEditor(Order.class,"orderItems.order", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (text != null && !"".equals(text)) {
			    		Order o =  orderService.getOrderById(UUID.fromString(text)); 
			            setValue(o);
			    	} else {
			    		setValue(null);	
			    	}
			    }
			    });			  
			binder.registerCustomEditor(Sku.class,"orderItems.sku", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (text != null && !"".equals(text)) {
			    		Sku sku =  productService.getSkuByIdForForm(UUID.fromString(text)); 
			            setValue(sku);
			    	} else {
			    		setValue(null);	
			    	}
			    }
			    });			  
			binder.registerCustomEditor(Customer.class, "customer", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (text != null && !"".equals(text)) {
			    		Customer cs = customerService.loadCustomerById(UUID.fromString(text), false);
			            setValue(cs);
			    	}			    
			    }
			    });
			binder.registerCustomEditor(Delivery.class, "actualFormFulfillment.delivery", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (text != null && !"".equals(text)) {
			    		Delivery cs = deliveryService.loadDeliveryById(UUID.fromString(text), false);
			            setValue(cs);
			    	}			    
			    }
			    });
			binder.registerCustomEditor(Store.class, "actualFormFulfillment.store", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (text != null && !"".equals(text)) {
			    		Store cs = deliveryService.loadStoreById(UUID.fromString(text), false);
			            setValue(cs);
			    	}			
			    }
			    });
			binder.registerCustomEditor(Fulfillment.class,"fulfillment", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		Fulfillment f = orderService.getFulfillmentById(UUID.fromString(text)); 
			            setValue(f);
			    	}			    
			    }
			    });			
			binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	  }
	  
	  @RequestMapping(method=GET)
	  public String orders() {
		  return "/admin/order/order"; 
		  }		  	  
	  
	  
	  @RequestMapping("/edit")
	  public String orderEdit(@RequestParam(value = "ID", required = false) String orderID, Model model,
			   				  @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  if(!model.containsAttribute("ord")) {
			 Order order = orderService.getOrderById(UUID.fromString(orderID));
			 for (OrderItem oi: order.getOrderItems()) {
				 oi.setImage(oi.getSku().lookupImages().size() > 0? oi.getSku().lookupImages().get(0): null);
			 }
		     model.addAttribute("ord", new OrderForm(order));

		  }
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/order/orderEdit :: editOrderForm";
            }		  
          return "/admin/order/orderEdit";		  
	  }
	  
	  @RequestMapping("/add")
	  public String orderAdd(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
 	      model.addAttribute("ord", new OrderForm());
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/order/orderEdit :: editOrderForm";
            } 	      
          return "/admin/order/orderEdit";	
	  }	 

	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String saveOrder(
			                   @Valid OrderForm order,
				   			   final BindingResult bindingResult,
			                   final RedirectAttributes ra
			                         ) throws InventoryUnavailableException {
		   orderValidator.validate(order, bindingResult);
		   if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("ord", order);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.ord", bindingResult);
	            return "redirect:/admin/order/edit";
	        }

	        orderService.mergeWithExistingAndUpdateOrCreate(order);	
	        return "redirect:/admin/order";
	    }
	  
	  @RequestMapping(value="/add-item", method = RequestMethod.POST )
	  public String addNewItem(
			   				   final @RequestParam(value = "skuId", required = true) String skuID,
			   				   final @RequestHeader(value = "X-Requested-With", required = true) String requestedWith,
			   				   final @ModelAttribute("order") OrderForm order,
			                   final BindingResult bindingResult,
			                   final Model model
			                         ) throws CloneNotSupportedException {
		   if (!"XMLHttpRequest".equals(requestedWith)) throw new IllegalStateException("The addNewItem method can be called only via ajax!");
           Sku sku = productService.getSkuById(UUID.fromString(skuID)); 
		   if (sku == null ) throw new SkuNotFoundException("The sku id="+skuID+" is empty!");
		   OrderItem foi = order.findOrderItem(sku);
		   if (foi == null) {
			   OrderItem oi=new OrderItem();
			   oi.setSku(sku);
			   productDisplayNameModificator.setSku(sku);
			   oi.setProductName(productDisplayNameModificator.getModifyedDisplayName(sku.lookupName()));
			   oi.setImage(sku.lookupImages().size() > 0? sku.lookupImages().get(0): null);
			   oi.setPrice(sku.getPriceForPackage());
			   oi.setQuantity(1);
			   order.addOrderItem(oi);
		   } else {
			   foi.setQuantity(foi.getQuantity()+1);
		   }
		   order.setDeliveryTotal(order.calculateDelivaryTotal());
		   order.setSubTotal(order.calculateSubTotal());
		   order.setTotal(order.calculateTotal());
		   model.addAttribute("ord", order);
	       return "/admin/order/orderEdit :: orderItemTable";
	   }	
	   
	  @RequestMapping(value="/update", method = RequestMethod.POST )
	  public String updateOrder(final @RequestHeader(value = "X-Requested-With", required = true) String requestedWith,
			   				    final @ModelAttribute("order") OrderForm order,
			                    final BindingResult bindingResult,
			                    final Model model
			                         ) {
		   if (!"XMLHttpRequest".equals(requestedWith)) throw new IllegalStateException("The addNewItem method can be called only via ajax!");
		   order.setDeliveryTotal(order.calculateDelivaryTotal());
		   order.setSubTotal(order.calculateSubTotal());
		   order.setTotal(order.calculateTotal());
		   model.addAttribute("ord", order);
	       return "/admin/order/orderEdit :: orderItemTable";
	   }	

	  
	  @RequestMapping(value="/del-item", method = RequestMethod.POST )
	  public String delItem ( 
							 final @RequestParam(value = "ID", required = true) String oiID,
							 final @RequestHeader(value = "X-Requested-With", required = true) String requestedWith,
							 final @ModelAttribute("order") OrderForm order,
					         final BindingResult bindingResult,
					         final Model model ) {
		   if (!"XMLHttpRequest".equals(requestedWith)) throw new IllegalStateException("The addNewItem method can be called only via ajax!");
		   order.removeOrderItem(UUID.fromString(oiID));
		   order.setDeliveryTotal(order.calculateDelivaryTotal());
		   order.setSubTotal(order.calculateSubTotal());
		   order.setTotal(order.calculateTotal());
		   model.addAttribute("ord", order);
		   return "/admin/order/orderEdit :: orderItemTable";
	  }
	  
	  @RequestMapping(value="/del-ff", method = RequestMethod.POST )
	  public String delff( 
							 final @RequestParam(value = "ID", required = true) String oiID,
							 final @RequestHeader(value = "X-Requested-With", required = true) String requestedWith,
							 final @ModelAttribute("order") OrderForm order,
					         final BindingResult bindingResult,
					         final Model model ) {
		   if (!"XMLHttpRequest".equals(requestedWith)) throw new IllegalStateException("The addNewItem method can be called only via ajax!");
		   order.removeFulfillment(UUID.fromString(oiID));
		   model.addAttribute("ord", order);
		   return "/admin/order/orderEdit :: ffHistoryTable";
	  }	  
          
}
