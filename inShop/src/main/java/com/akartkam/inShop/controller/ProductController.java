package com.akartkam.inShop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.rating.RatingType;
import com.akartkam.inShop.service.rating.RatingReviewService;

@Controller
public class ProductController extends WebEntityAbstractController {
	private static final Log LOG = LogFactory.getLog(ProductController.class);
	
	@Value("#{entityUrlPrefixes.getProperty(T(com.akartkam.inShop.util.Constants).PRODUCT_CLASS)}")
	private String productPrefix;
	
	@Autowired
	private RatingReviewService ratingReviewService;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		super.handleRequestInternal(request, response);
		String productUrl = request.getServletPath();
		productUrl = productUrl.replace("/"+productPrefix, "");
		Product product = productService.getProductByUrl(productUrl);
		if (product != null) {
			model.addObject("product", product);
			model.addObject("ratingSummary", ratingReviewService
					          .readRatingSummary(product.getId().toString(), RatingType.forName("PRODUCT")));
			model.setViewName("/catalog/single-product");			
		}else {
			response.setStatus(404);
			model.setViewName("/errors/error-default");	
		}
		return model;
	}

}
