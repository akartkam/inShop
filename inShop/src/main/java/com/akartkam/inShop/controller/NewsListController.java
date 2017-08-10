package com.akartkam.inShop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.akartkam.inShop.domain.content.NewsPage;

@Controller
public class NewsListController extends WebEntityAbstractController {


	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		initDefault();
		List<NewsPage> news = contentService.getActualNewsPages();		
		model.addObject("news", news);
		model.setViewName("/content/news-list");
		return model;
	}

}
