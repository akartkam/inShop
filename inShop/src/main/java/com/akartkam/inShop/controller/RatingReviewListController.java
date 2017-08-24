package com.akartkam.inShop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.akartkam.inShop.domain.rating.ReviewDetail;
import com.akartkam.inShop.service.rating.RatingReviewService;

@Controller
public class RatingReviewListController extends WebEntityAbstractController {

	@Autowired
	private RatingReviewService ratingReviewService;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		initDefault();
		List<ReviewDetail> rdl = ratingReviewService.readCommonReviewDetails();
		model.addObject("reviewList", rdl);
		model.setViewName("/rating/review-list");
		return model;
	}

}
