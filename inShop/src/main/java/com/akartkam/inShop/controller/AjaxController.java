package com.akartkam.inShop.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.formbean.ItemsForJSON;
import com.akartkam.inShop.formbean.SkuForJSON;
import com.akartkam.inShop.service.extension.EntityUrlModificator;
import com.akartkam.inShop.service.order.InventoryService;
import com.akartkam.inShop.service.product.ProductService;


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
	  
	
}