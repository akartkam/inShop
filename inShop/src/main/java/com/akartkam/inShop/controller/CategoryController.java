package com.akartkam.inShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.service.product.CategoryService;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	

	@RequestMapping("/category/**")
	public String handleCategory(Model model) {
		Category category = categoryService.getCategoryByUrl("/sredstva-uhoda-za-stomoi");
		model.addAttribute("category", category);
		return "/catalog/category";
	}


}
