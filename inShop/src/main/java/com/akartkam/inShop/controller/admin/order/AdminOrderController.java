package com.akartkam.inShop.controller.admin.order;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.customer.Customer;
import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.order.OrderItem;
import com.akartkam.inShop.domain.order.OrderStatus;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.ProductStatus;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.akartkam.inShop.exception.SkuNotFoundException;
import com.akartkam.inShop.formbean.AccountForm;
import com.akartkam.inShop.formbean.ItemsForJSON;
import com.akartkam.inShop.formbean.ProductForm;
import com.akartkam.inShop.formbean.SkuForJSON;
import com.akartkam.inShop.service.customer.CustomerService;
import com.akartkam.inShop.service.order.OrderService;
import com.akartkam.inShop.service.product.ProductService;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {
	  private static final Log LOG = LogFactory.getLog(AdminOrderController.class);

	  @Autowired
	  ProductService productService;

	  @Autowired
	  OrderService orderService;
	  
	  @Autowired
	  CustomerService customerService; 
	  
	  @ModelAttribute("allOrders")
	  public List<Order> getAllOrders() {
		  return orderService.getAllOrders();
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
	  
	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
		    binder.setAllowedFields(new String[] {"id", "customer", "status", "submitDate", "orderNumber", "emailAddress", "orderItems*" });
		    binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });
			binder.registerCustomEditor(OrderItem.class,"orderItems", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		OrderItem oi =  orderService.getOrderItemById(UUID.fromString(text)); 
			            setValue(oi);
			    	} else {
			    		setValue(null);	
			    	}
			    }
			    });			  
			binder.registerCustomEditor(Sku.class,"orderItems.sku", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		Sku sku =  productService.getSkuById(UUID.fromString(text)); 
			            setValue(sku);
			    	} else {
			    		setValue(null);	
			    	}
			    }
			    });			  
			binder.registerCustomEditor(ProductOptionValue.class,"orderItems.sku.productOptionValuesList", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		ProductOptionValue pov =  productService.getPOVById(UUID.fromString(text)); 
			            setValue(pov);
			    	} else {
			    		setValue(null);	
			    	}
			    }
			    });		

			binder.registerCustomEditor(Customer.class, "customer", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		Customer cs = customerService.loadCustomerById(UUID.fromString(text), false);
			            setValue(cs);
			    	}			    
			    }
			    });
			
			/*PropertyEditor pe = new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		SimpleDateFormat formatter = new SimpleDateFormat(messageSource.getMessage("date.formatshort", null, Locale.getDefault()));
			    		try {
			    			Date date = formatter.parse(text);
				            setValue(date);
			    		} catch (ParseException e) {
			    			LOG.error(e);
			    		}			    		

			    	} else {
			    		setValue(null);
			    	}
			    }
			    };

			binder.registerCustomEditor(java.util.Date.class,"submitDate", pe);*/
	  }
	  
	  @RequestMapping(method=GET)
	  public String orders() {
		  return "/admin/order/order"; 
		  }		  
	  
	  @RequestMapping(value="/product-search", method= RequestMethod.GET, produces="application/json")
	  @ResponseStatus(HttpStatus.OK)	  
	  public @ResponseBody ItemsForJSON searchProductsForOrder(@RequestParam(value = "q", required = true) String q, Model model) {
		  List<SkuForJSON> skus = productService.getSkusForJSONByName('%'+q+'%');
		  ItemsForJSON items = new ItemsForJSON(skus);
		  return items;
		  
	  }
	  
	  @RequestMapping("/edit")
	  public String orderEdit(@RequestParam(value = "ID", required = false) String orderID, Model model,
			   				  @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  if(!model.containsAttribute("ord")) {
			 Order order = orderService.getOrderById(UUID.fromString(orderID));
		     model.addAttribute("ord", order);
		  }
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/order/orderEdit :: editOrderForm";
            }		  
          return "/admin/order/orderEdit";		  
	  }
	  
	  @RequestMapping("/add")
	  public String orderAdd(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  Order order = new Order();
 	      model.addAttribute("ord", order);
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/order/orderEdit :: editOrderForm";
            } 	      
          return "/admin/order/orderEdit";		  
	  }	  

	  @RequestMapping(value="/add-item", method = RequestMethod.POST )
	  public String addNewItem(
			   				   final @RequestParam(value = "skuId", required = true) String skuID,
			   				         @RequestHeader(value = "X-Requested-With", required = true) String requestedWith,
			   				   final @ModelAttribute("order") Order order,
			                   final BindingResult bindingResult,
			                   final Model model
			                         ) throws CloneNotSupportedException {
		   if (!"XMLHttpRequest".equals(requestedWith)) throw new IllegalStateException("The addNewItem method can be called only via ajax!");
           Sku sku = productService.getSkuById(UUID.fromString(skuID)); 
		   if (sku == null ) throw new SkuNotFoundException("The sku id="+skuID+" is empty!");
		   if (!order.isContainsSku(sku)) {
			   OrderItem oi=new OrderItem();
			   oi.setSku(sku);
			   oi.setPrice(sku.getSalePrice() != null ? sku.getSalePrice() : sku.getRetailPrice());
			   oi.setProduct(sku.getDefaultProduct() != null ? sku.getDefaultProduct() : sku.getProduct());
			   oi.setOrder(order);
			   order.getOrderItems().add(oi);
		   }
		   model.addAttribute("ord", order);
		   model.addAttribute("tabactive","content");
	       return "/admin/order/orderEdit :: editOrderForm";
	   }	   


}
