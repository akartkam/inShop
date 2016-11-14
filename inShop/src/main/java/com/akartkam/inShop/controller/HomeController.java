package com.akartkam.inShop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.service.product.CategoryService;


@Controller
public class HomeController extends AbstractController {
	
	private static final Log LOG = LogFactory.getLog(HomeController.class);

	@Autowired
	private CategoryService categoryService;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Category> rootCategorys = categoryService.getRootCategories(false);
		ModelAndView model = new ModelAndView("/layouts/home");
		model.addObject("rootCategorys", rootCategorys);
		return model;
	}

}
