package com.akartkam.inShop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.akartkam.inShop.common.filter.ProductFilterBrandConditionHolder;
import com.akartkam.inShop.common.filter.ProductFilterCategoryConditionHolder;
import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.formbean.ProductFilterDTO;

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
			ProductFilterDTO pd =  productService.getProductFilterDTO(new ProductFilterBrandConditionHolder(brand));
			pd.setDropFilterUrl(brandPrefix+brand.getUrl());
			model.addObject("filterDTO", pd);
			List<Product> products = brandService.getProductsByBrand(brand);	
			model.addObject("brand", brand);
			model.addObject("products", products);
			model.setViewName("/catalog/brand-products");			
		} else {
			response.setStatus(404);
			model.setViewName("/errors/error-default");
		}

		return model;
	}

}
