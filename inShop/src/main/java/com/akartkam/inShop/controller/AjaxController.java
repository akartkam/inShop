package com.akartkam.inShop.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.number.AbstractNumberFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.akartkam.inShop.domain.order.Delivery;
import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.formbean.DataTableForm;
import com.akartkam.inShop.formbean.DataTableJSON;
import com.akartkam.inShop.formbean.ItemsForJSON;
import com.akartkam.inShop.formbean.SkuForJSON;
import com.akartkam.inShop.service.extension.EntityUrlModificator;
import com.akartkam.inShop.service.order.DeliveryService;
import com.akartkam.inShop.service.order.InventoryService;
import com.akartkam.inShop.service.order.OrderService;
import com.akartkam.inShop.service.product.ProductService;
import com.fasterxml.jackson.core.io.JsonStringEncoder;


@Controller
public class AjaxController {
	
	private static final Log LOG = LogFactory.getLog(AjaxController.class);
	
	private static final int ROWS_PER_PAGE = 20;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private EntityUrlModificator entityUrlModificator;	
	
	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private AbstractNumberFormatter currencyNumberFormatter;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping("/ajax-quick-review-product")
	public String qRevProdAjax(@RequestParam(value = "ID", required = true) String productID, Model model,
			   				   @RequestHeader(value = "X-Requested-With", required = true) String requestedWith) {
		if (!"".equals(productID)) {
		  try {
			  UUID id = UUID.fromString(productID);
			  Product product = productService.getProductById(id);
			  if (product == null || !product.isEnabled()) {
				  LOG.error("Error in ajax-quick-review-product prodId="+productID); 
				  return "/admin/Error";}
			  Map<Sku, Boolean> skuAvailableMap;
			  if (product.hasAdditionalSkus()) {
				  skuAvailableMap = inventoryService.retrieveIsAvailable(product.getSkus());
			  } else {
				  skuAvailableMap = inventoryService.retrieveIsAvailable(Arrays.asList(new Sku[]{product.getDefaultSku()})); 
			  }
			  model.addAttribute("skuAvailableMap", skuAvailableMap);
			  model.addAttribute("product", product);
		  } catch (IllegalArgumentException e) {
			  LOG.error("Error in ajax-quick-review-product prodId="+productID, e); 
			  return "/admin/Error";	   
		  }
		  return "/catalog/partials/quickReviewProduct";
		}
		return "/admin/Error";
	}
	
	@RequestMapping("/ajax-product-options-modal")
	public String mProdOptsAjax(@RequestParam(value = "ID", required = true) String productID, Model model,
			   				    @RequestHeader(value = "X-Requested-With", required = true) String requestedWith) {
		if (!"".equals(productID)) {
			  try {
				  UUID id = UUID.fromString(productID);
				  Product product = productService.getProductById(id);
				  if (product == null || !product.isEnabled()){
					  LOG.error("Error in ajax-product-options-modal="+productID);
					  return "/admin/Error";	
				  }
				  model.addAttribute("product", product);
			  } catch (IllegalArgumentException e) {
				  LOG.error("Error in ajax-product-options-modal="+productID, e);
					 return "/admin/Error";	   
			  }
			  return "/catalog/partials/modalProductOptions";
		}
		return "/admin/Error";				  
	}
	
	  @RequestMapping(value="/product-search", method= RequestMethod.GET, produces="application/json")
	  @ResponseStatus(HttpStatus.OK)	  
	  public @ResponseBody ItemsForJSON searchProductsForOrder(@RequestParam(value = "q", required = true) String q,
			  												   @RequestParam(value = "page", required = false) String page,
			  													Model model) {
		  int curPage = 0;
		  if (page != null && !"".equals(page)) curPage = Integer.parseInt(page);
		  Object[] skusArr = productService.getSkusForJSONByName('%'+q+'%', ROWS_PER_PAGE, curPage);
		  Long totalRows = (Long)skusArr[0];
		  List<SkuForJSON> skusForJson = (List<SkuForJSON>)skusArr[1];
		  ItemsForJSON items = new ItemsForJSON(skusForJson, totalRows);
		  return items;
		  
	  }	
	  
	  @RequestMapping(value="/product-by-sku/{skuId}", method= RequestMethod.GET)
	  public String getProductBySku(@PathVariable("skuId") String skuId, Model model){
		   Sku sku = productService.getSkuById(UUID.fromString(skuId));
		   Product product = null;
		   String goToUrl = "redirect:/error-default";
		   if (sku != null) {
			   product = sku.isDefaultSku()? sku.getDefaultProduct(): sku.getProduct();
			   if (product != null) {
				  entityUrlModificator.setEntity(product);
				  String preffixedUrl = entityUrlModificator.getPrefixedUrl(product.getUrl());
				  goToUrl = "redirect:"+preffixedUrl;
			   }
		   }
		   return goToUrl;
	  }
	  
	  @RequestMapping("/ajax-delivery-details")
	  public String getDeliveryDetailsAjax(@RequestParam(value = "ID", required = true) String deliveryID, Model model,
				   				           @RequestHeader(value = "X-Requested-With", required = true) String requestedWith) {
			if (!"".equals(deliveryID)) {
				  try {
					  UUID id = UUID.fromString(deliveryID);
					  Delivery delivery = deliveryService.getDeliveryById(id);
					  if (delivery == null || !delivery.isEnabled()){
						  LOG.error("Error in ajax-delivery-details="+deliveryID);
						  return "/admin/Error";	
					  }
					  model.addAttribute("dv", delivery);
				  } catch (IllegalArgumentException e) {
					  LOG.error("Error in ajax-delivery-details="+deliveryID, e);
						 return "/admin/Error";	   
				  }
				  return "/order/partials/deliveryDetails";
			}
			return "/admin/Error";		  
	  }

	  @RequestMapping(value="/product-ajax-load", method= RequestMethod.GET, produces="application/json")
	  @ResponseStatus(HttpStatus.OK)	  
	  public @ResponseBody DataTableJSON productDataTableJSON (@ModelAttribute DataTableForm dataTableForm,
			  									               Model model) {
		  Object[] dtArr = productService.getProductsForDataTable(dataTableForm);
		  Long countRecFiltered = (Long) dtArr[0]; 
		  List<Product> retProducts = (List<Product>)dtArr[1];
		  DataTableJSON items = new DataTableJSON();
		  items.setDraw(dataTableForm.getDraw());
		  items.setRecordsTotal(productService.countTotalProducts());
		  items.setRecordsFiltered(countRecFiltered);
		  String[][] data = new String [retProducts.size()][9];
		  JsonStringEncoder e = JsonStringEncoder.getInstance();
		  for (int i=0; i <= retProducts.size()-1; i++){
			  Product p = retProducts.get(i);
			  StringBuilder sb = new StringBuilder();
			  e.quoteAsString(p.getDefaultSku().getName(), sb);
			  data[i][0] = "{\"name\":\""+sb.toString()+
					         "\", \"codes\":\""+p.getSkuCodes().toString()+"\",\"image\":\""+
					       (p.getAllImages().size()!=0 ? p.getAllImages().get(0):"") +"\"}";
			  data[i][1] = p.getCategory().buildFullName();
			  data[i][2] = p.getUrl();
			  data[i][3] = p.getBrand().getName();
			  data[i][4] = p.getModel();
			  data[i][5] = currencyNumberFormatter.print(p.getDefaultSku().getPrice(), Locale.getDefault());
			  data[i][6] = p.getOrdering().toString();
			  data[i][7] = p.isEnabled()? "y": "";
			  data[i][8] = "{\"name\":\""+sb.toString()+"\", \"id\":\""+p.getId()+"\"}";
		  }
		  items.setData(data);
		  return items;
		  
	  }

	  @RequestMapping(value="/order-ajax-load", method= RequestMethod.GET, produces="application/json")
	  @ResponseStatus(HttpStatus.OK)	  
	  public @ResponseBody DataTableJSON orderDataTableJSON (@RequestParam(value = "status", required = false) String orderStatus,
			  												 @ModelAttribute DataTableForm dataTableForm,
			  									             Model model) {
		  Object[] dtArr;
		  if (orderStatus != null && !"".equals(orderStatus)){
			  dtArr = orderService.getProductsForDataTable(dataTableForm, orderStatus); 
		  } else {
			  dtArr = orderService.getProductsForDataTable(dataTableForm);  
		  }
		  
		  Long countRecFiltered = (Long) dtArr[0]; 
		  List<Order> retOrders = (List<Order>)dtArr[1];
		  DataTableJSON items = new DataTableJSON();
		  items.setDraw(dataTableForm.getDraw());
		  items.setRecordsTotal(orderService.countTotalOrders());
		  items.setRecordsFiltered(countRecFiltered);
		  String[][] data = new String [retOrders.size()][7];
		  for (int i=0; i <= retOrders.size()-1; i++){
			  Order o = retOrders.get(i);
			  data[i][0] = o.getOrderNumber();
			  data[i][1] = (new SimpleDateFormat("dd.MM.YYYY HH:mm")).format(o.getSubmitDate());
			  data[i][2] = o.getCustomer() != null? o.getCustomer().getFullName(): "";
			  data[i][3] = o.getEmailAddress();
			  data[i][4] = currencyNumberFormatter.print(o.getTotal(), Locale.getDefault());
			  data[i][5] = "{\"status\":\""+o.getStatus().toString() +"\", \"label\":\""+
			                 messageSource.getMessage("order.status."+o.getStatus(), null, Locale.getDefault()) +"\"}";
			  data[i][6] = "{\"id\":\""+o.getId()+"\"}";
		  }
		  items.setData(data);
		  return items;
		  
	  }
	  
	  
}