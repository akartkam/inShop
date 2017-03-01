package com.akartkam.inShop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.akartkam.inShop.domain.content.Page;
import com.akartkam.inShop.service.content.ContentService;

@Controller
public class PageController extends WebEntityAbstractController {

	@Value("#{entityUrlPrefixes.getProperty(T(com.akartkam.inShop.util.Constants).PAGE_CLASS)}")
	private String pagePrefix;
	
	@Autowired
	private ContentService contentService;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		super.handleRequestInternal(request, response);
		String pageUrl = request.getServletPath();
		pageUrl = pageUrl.replace("/"+pagePrefix, "");
		Page page = contentService.getPageByUrl(pageUrl);
		if (page != null) {
			model.addObject("page", page);
			model.setViewName("/content/page");			
		} else {
			String viewName = pageUrl.substring(pageUrl.lastIndexOf("/"));
			model.setViewName("/content"+viewName);	
		}
		return model;
	}

}
