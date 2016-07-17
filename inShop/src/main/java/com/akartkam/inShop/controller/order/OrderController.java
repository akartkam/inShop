package com.akartkam.inShop.controller.order;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.formbean.ItemsForJSON;
import com.akartkam.inShop.formbean.SkuForJSON;
import com.akartkam.inShop.service.product.ProductService;

@Controller
@RequestMapping("/order")
public class OrderController {
	  private static final Log LOG = LogFactory.getLog(OrderController.class);

	  @Autowired
	  ProductService productService;
	  
	  @RequestMapping(value="/product-search", method= RequestMethod.GET, produces="application/json")
	  @ResponseStatus(HttpStatus.OK)	  
	  public @ResponseBody ItemsForJSON searchProductsForOrder(@RequestParam(value = "q", required = true) String q, Model model) {
		  List<SkuForJSON> skus = productService.getSkusForJSONByName('%'+q+'%');
		  ItemsForJSON items = new ItemsForJSON(skus);
		  return items;
		  
	  }
	  
	  @RequestMapping(value="/test", method= RequestMethod.GET)
	  public String testOrder() {
		  return "/productsForOrder";		  
	  }


}
