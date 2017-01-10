package com.akartkam.inShop.controller;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.ProductStatus;
import com.akartkam.inShop.service.product.BrandService;
import com.akartkam.inShop.service.product.CategoryService;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.util.CartUtil;


@Controller
public class MainController extends WebEntityAbstractController {
	
	private static final Log LOG = LogFactory.getLog(MainController.class);


	@Resource
	@Qualifier("mainSliderBanner")
	private Map<String, Properties> mainSliderBanner;
	
	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		initDefault();
		model.setViewName("redirect:/admin");
		//model.setViewName("/test");
		return model;
		//List<Product> newProducts = productService.getProductsByProductStatus(ProductStatus.NEW);
		/*List<Product> actionProducts = productService.getProductsByProductStatus(ProductStatus.ACTION);
		List<Product> hitProducts = productService.getProductsByProductStatus(ProductStatus.HIT);
		List<Brand> brands = brandService.getAllBrand(false);
		model.setViewName("/layouts/home");
		model.addObject("hitProducts", hitProducts);
		model.addObject("actionProducts", actionProducts);
		model.addObject("brands", brands);
		model.addObject("mainSliderBanner", mainSliderBanner);
		CartUtil.getCartFromSession(request);
		return model;*/
	}
	
	

}
