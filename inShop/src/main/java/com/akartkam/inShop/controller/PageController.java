package com.akartkam.inShop.controller;

import java.io.File;

import javax.servlet.ServletContext;
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
	
	@Autowired
	private ServletContext servletContext;	
	
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
			if (pageUrl != null && !"".equals(pageUrl)){
				String viewName = pageUrl.substring(pageUrl.lastIndexOf("/"));
				String rPath = servletContext.getRealPath("/WEB-INF/templates/content"+viewName+".html");
				File f = null;
				if (rPath != null && !"".equals(rPath)) {
					f = new File(rPath);	
				}
				if(f != null && f.exists() && !f.isDirectory()) { 
				   model.setViewName("/content"+viewName);
				} else {
					response.setStatus(404);
					model.setViewName("/errors/error-default");				}
				
			} else {
				response.setStatus(404);
				model.setViewName("/errors/error-default");			}
		}
		return model;
	}

}
