package com.akartkam.inShop.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AjaxController {

	  @RequestMapping("/ajax-quick-review-product")
	  public String qRevProdAjax(@RequestParam(value = "ID", required = false) String productID, Model model,
			   				     @RequestHeader(value = "X-Requested-With", required = true) String requestedWith) {

		  return "/catalog/partials/quickReviewProduct";
	  }
}
