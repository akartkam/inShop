package com.akartkam.inShop.controller.admin;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring.support.Layout;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.service.product.CategoryService;

@Controller
@RequestMapping("/admin")
@Layout("layouts/admin-default")
public class AdminController {
	
	  @Autowired
	  CategoryService categoryService;
	
	  @ModelAttribute("allRootCategories")
	  public List<Category> allCategories() {
	      return this.categoryService.getRootCategories();
	  }
	
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