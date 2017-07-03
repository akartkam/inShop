package com.akartkam.inShop.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.formbean.ProductFilterDTO;
import com.akartkam.inShop.service.product.ProductService;

@Controller
public class CategoryController extends WebEntityAbstractController {
	private static final Log LOG = LogFactory.getLog(CategoryController.class);


	@Value("#{entityUrlPrefixes.getProperty(T(com.akartkam.inShop.util.Constants).CATEGORY_CLASS)}")
	private String categoryPrefix;

	@Autowired
	private ProductService productService;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.handleRequestInternal(request, response);
		Map<String, Object> modelMap = model.getModel();
		Category category = (Category) modelMap.get("category");
		if (category == null) {
			String categoryUrl = request.getServletPath();
			categoryUrl = categoryUrl.replace("/"+categoryPrefix, "");
			category = categoryService.getCategoryByUrl(categoryUrl);
			model.addObject("category", category);
		}
		if (category != null) {
			if (!modelMap.containsKey("filterDTO")) {
				ProductFilterDTO pd =  productService.getProductFilterDTOByCategory(category.getId());
				pd.setDropFilterUrl(categoryPrefix+category.getUrl());
				model.addObject("filterDTO", pd);				
			}
			model.setViewName("/catalog/category");
		} else {
			response.setStatus(404);
			model.setViewName("/errors/error-default");
		}
		/*LOG.info("getServletPath() - "+request.getServletPath());
		LOG.info("getContextPath() - "+request.getContextPath());
		LOG.info("getLocalAddr() - "+request.getLocalAddr());
		LOG.info("getLocalName() - "+request.getLocalName());
		LOG.info("getLocalPort() - "+request.getLocalPort());
		LOG.info("getMethod() - "+request.getMethod());
		LOG.info("getPathInfo() - "+request.getPathInfo());
		LOG.info("getProtocol() - "+request.getProtocol());
		LOG.info("getQueryString() - "+request.getQueryString());
		LOG.info("getRequestedSessionId() - "+request.getRequestedSessionId());
		LOG.info("getRequestURI() - "+request.getRequestURI());
		LOG.info("getRequestURL() - "+request.getRequestURL());
		LOG.info("getScheme() - "+request.getScheme());
		LOG.info("getServerName() - "+request.getServerName());
		LOG.info("getServerPort() - "+request.getServerPort());
		LOG.info("getParameterNames() - "+request.getParameterNames());*/
		return model;
	}


}
