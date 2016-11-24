package com.akartkam.inShop.controller;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.service.product.ProductService;


@Controller
public class AjaxController {
	
	@Autowired
	private ProductService productService;

	@RequestMapping("/ajax-quick-review-product")
	public String qRevProdAjax(@RequestParam(value = "ID", required = true) String productID, Model model,
			   				   @RequestHeader(value = "X-Requested-With", required = true) String requestedWith) {
		if (!"".equals(productID)) {
		  try {
			  UUID id = UUID.fromString(productID);
			  Product product = productService.getProductById(id);
			  if (product == null || !product.isEnabled()) return "/admin/Error";
			  model.addAttribute("product", product);
		  } catch (IllegalArgumentException e) {
			 return "/admin/Error";	   
		  }
		  return "/catalog/partials/quickReviewProduct";
		}
		return "/admin/Error";
	}
}