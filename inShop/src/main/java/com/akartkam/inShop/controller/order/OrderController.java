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
import com.akartkam.inShop.service.product.ProductService;

@Controller
@RequestMapping("/order")
public class OrderController {
	  private static final Log LOG = LogFactory.getLog(OrderController.class);

	  @Autowired
	  ProductService productService;
	  
	  @RequestMapping(value="/product-search", method= RequestMethod.GET, produces="application/json")
	  @ResponseStatus(HttpStatus.OK)	  
	  public @ResponseBody List<Sku> searchProductsForOrder(@RequestParam(value = "q", required = true) String q, Model model,
              							   @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  //if (!"XMLHttpRequest".equals(requestedWith)) throw new IllegalStateException("The addNewImage method can be called only via ajax!");
		  List<Sku> skus = productService.getSkusByName('%'+q+'%');
		  List<FoundSku> fSku = new ArrayList<FoundSku>(10);
		  for (Sku sku: skus) {
			  fSku.add(new FoundSku(sku.getName(), sku.getImages())); 
		  }
		  return skus;
		  
	  }
	  
	  private static class FoundSku {
		  private String name;
		  private String images[];
		  
		  public FoundSku (String name, List<String> images) {
			  this.name = name;
			  this.images = images.toArray(new String[0]);
		  }
		  
		  public String getName() {
				return name;
			}

		  public String[] getImages() {
				return images;
			}

		  
	  }

}
