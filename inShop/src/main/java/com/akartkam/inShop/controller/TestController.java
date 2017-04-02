package com.akartkam.inShop.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/catalog/test")
public class TestController {
	  @RequestMapping(value="/product-ajax-test", method=GET)
	  public String test() {
		  return "/admin/catalog/product-ajax-load-test"; 
		  }	

}
