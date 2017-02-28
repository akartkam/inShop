package com.akartkam.inShop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.akartkam.inShop.domain.product.Category;

@Controller
public class CategoryController extends WebEntityAbstractController {
	private static final Log LOG = LogFactory.getLog(CategoryController.class);


	@Value("#{entityUrlPrefixes.getProperty(T(com.akartkam.inShop.util.Constants).CATEGORY_CLASS)}")
	private String categoryPrefix;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.handleRequestInternal(request, response);
		String categoryUrl = request.getServletPath();
		categoryUrl = categoryUrl.replace("/"+categoryPrefix, "");
		Category category = categoryService.getCategoryByUrl(categoryUrl);
		if (category != null) {
			model.addObject("category", category);
			model.setViewName("/catalog/category");
		} else {
			
		}
		/*LOG.info(request.getServletPath());
		LOG.info(request.getContextPath());
		LOG.info(request.getLocalAddr());
		LOG.info(request.getLocalName());
		LOG.info(request.getLocalPort());
		LOG.info(request.getMethod());
		LOG.info(request.getPathInfo());
		LOG.info(request.getProtocol());
		LOG.info(request.getQueryString());
		LOG.info(request.getRequestedSessionId());
		LOG.info(request.getRequestURI());
		LOG.info(request.getRequestURL());
		LOG.info(request.getScheme());
		LOG.info(request.getServerName());
		LOG.info(request.getServerPort());
		LOG.info(request.getServletPath());
		LOG.info(request.getParameterNames());*/
		return model;
	}


}
