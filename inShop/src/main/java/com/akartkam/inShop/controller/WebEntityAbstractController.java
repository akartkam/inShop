package com.akartkam.inShop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.service.product.BrandService;
import com.akartkam.inShop.service.product.CategoryService;
import com.akartkam.inShop.service.product.ProductService;

public abstract class WebEntityAbstractController extends AbstractController {
	@Autowired
	protected CategoryService categoryService;

	@Autowired
	protected ProductService productService;	
	
	@Autowired
	protected BrandService brandService;
	
	protected ModelAndView model = new ModelAndView();
	
	protected List<Category> rootCategorys;
	
	public void initDefault() {
		rootCategorys = categoryService.getRootCategories(false);
		model.addObject("rootCategorys", rootCategorys);
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		initDefault();
		return model;
	}
	


}
