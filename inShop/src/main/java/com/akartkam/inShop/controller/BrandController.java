package com.akartkam.inShop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Product;

@Controller
public class BrandController extends WebEntityAbstractController {

	
	@Value("#{entityUrlPrefixes.getProperty(T(com.akartkam.inShop.util.Constants).BRAND_CLASS)}")
	private String brandPrefix;	
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		super.handleRequestInternal(request, response);
		String brandUrl = request.getServletPath();
		brandUrl = brandUrl.replace("/"+brandPrefix, "");
		Brand brand = brandService.getBrandByUrl(brandUrl);
		if (brand != null) {
			List<Product> products = brandService.getProductsByBrand(brand);	
			model.addObject("brand", brand);
			model.addObject("products", products);
			model.setViewName("/catalog/brand-products");			
		} else {
			model.setViewName("redirect:/error-default");	
		}

		return model;
	}

}
