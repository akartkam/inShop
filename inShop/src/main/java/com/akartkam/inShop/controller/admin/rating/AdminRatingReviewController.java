package com.akartkam.inShop.controller.admin.rating;


import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.akartkam.inShop.domain.rating.ReviewDetail;
import com.akartkam.inShop.domain.rating.ReviewStatusType;
import com.akartkam.inShop.service.rating.RatingReviewService;

@Controller
@RequestMapping("/admin/rating")
public class AdminRatingReviewController {
	
	@Autowired
	private RatingReviewService ratingReviewService;
	
	@Autowired
	private MessageSource messageSource;
	
    @ModelAttribute("allReviews")
    public List<ReviewDetail> getAllReviews() {
	      return ratingReviewService.getAllReviewDetail();
    }	
    
	@RequestMapping("/reviews")
	public String reviews() {
	    return "/admin/rating/ratingReview"; 
	}
	
	
	@RequestMapping(value = "/reviews/delete", method = RequestMethod.POST)
	public String removeReview(@RequestParam(value = "id", required = true) String id) {

		return "redirect:/admin/rating/reviews";
	}	
	
	@RequestMapping(value="/change-status", produces = "text/plain;charset=UTF-8")
	public @ResponseBody String changeStatus(@RequestParam(value = "id", required = true) String id,
			  								 @RequestParam(value = "status", required = true) String newStatus) throws UnsupportedEncodingException, NoSuchMessageException {
		 ReviewStatusType rt = ReviewStatusType.forName(newStatus);
		 boolean res = ratingReviewService.changeReviewDetailStatus(UUID.fromString(id), rt);
		 if (res) {
			 String rs = messageSource.getMessage("rating.reviewStatus."+newStatus, null, Locale.getDefault());
			 return rs;
		 }
		 else return "error";
	}

}
