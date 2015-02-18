package com.akartkam.inShop.controller.admin;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.SessionAttributes;
import org.thymeleaf.spring.support.Layout;

@Controller
@RequestMapping("/admin")
@Layout("layouts/admin-default")
//@SessionAttributes("isadmin")
public class AdminController {
	  @RequestMapping(method=GET)
	  public String admin(HttpSession  session) {
		  session.setAttribute("isadmin", new Boolean(true));
		  return "admin/admin"; 
		  }
	  @RequestMapping("/catalog/category")
	  public String category() {
		  return "/admin/category"; 
		  }	  
}