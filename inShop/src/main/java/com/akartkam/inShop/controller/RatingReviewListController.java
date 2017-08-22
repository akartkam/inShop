package com.akartkam.inShop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RatingReviewListController extends WebEntityAbstractController {


	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		initDefault();
		model.setViewName("/rating/review-list");
		return model;
	}

}
