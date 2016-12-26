package com.akartkam.inShop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.akartkam.inShop.domain.product.Brand;

@Controller
public class BrandListController extends WebEntityAbstractController {


	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		initDefault();
		List<Brand> brands = brandService.getAllBrand();
		model.addObject("brands", brands);
		model.setViewName("/catalog/brand-list");
		return model;
	}

}
