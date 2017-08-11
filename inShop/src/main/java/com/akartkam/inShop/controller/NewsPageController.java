package com.akartkam.inShop.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.akartkam.inShop.domain.content.NewsPage;

@Controller
public class NewsPageController extends WebEntityAbstractController {

	@Value("#{entityUrlPrefixes.getProperty(T(com.akartkam.inShop.util.Constants).NEWS_CLASS)}")
	private String pagePrefix;
	
	
	@Autowired
	private ServletContext servletContext;	
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		super.handleRequestInternal(request, response);
		String pageUrl = request.getServletPath();
		pageUrl = pageUrl.replace("/"+pagePrefix, "");
		NewsPage page = contentService.getNewsPageByUrl(pageUrl);
		if (page != null) {
			model.addObject("page", page);
			model.setViewName("/content/news-page");			
		} else {
				response.setStatus(404);
				model.setViewName("/errors/error-default");	
		}
		return model;
	}

}
