package com.akartkam.inShop.controller.admin;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	  @ModelAttribute("allCategories")
	  public List<Category> allCategories() {
	      return this.categoryService.getAllCategoryHierarchy();
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
	  
	  @Layout("disable")
	  @RequestMapping("/catalog/category/edit/{id}")
	  public String category(@PathVariable("id") String id, Model model) {
		  Category category = categoryService.getCategoryById(id);
		  model.addAttribute("category", category);
		  return "/admin/categoryEdit"; 
		  }	  
	  
}