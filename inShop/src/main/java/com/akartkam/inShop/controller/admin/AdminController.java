package com.akartkam.inShop.controller.admin;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
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

	
	  @RequestMapping(method=GET)
	  public String admin(HttpSession  session) {
		  session.setAttribute("isadmin", new Boolean(true));
		  return "admin/admin"; 
		  }
	  
	  @RequestMapping("/catalog/category")
	  public String category(Model model) {
		  model.addAttribute("allCategories", categoryService.getAllCategoryHierarchy());  
		  return "/admin/category"; 
		  }	  
	  
	  @Layout("disable")
	  @RequestMapping("/catalog/category/edit/{id}")
	  public String category(@PathVariable("id") String id, Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  Category category = categoryService.getCategoryById(id);
		  model.addAttribute("category", category);
          if ("XMLHttpRequest".equals(requestedWith)) {
            return "/admin/categoryEdit :: editCategoryForm";
          }
          return "/admin/categoryEdit";		  
		    
		  }	  
	  
}