package com.akartkam.inShop.controller.admin.rating;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @ModelAttribute("allReviews")
    public List<ReviewDetail> getAllReviews() {
	      return ratingReviewService.getAllReviewDetail();
    }	
    
	@RequestMapping("/reviews")
	public String reviews() {
	    return "/admin/rating/ratingReview"; 
	}
	
	
	@RequestMapping("/change-status")
	public @ResponseBody String changeStatus(@RequestParam(value = "ID", required = true) String id,
			  								 @RequestParam(value = "status", required = true) String newStatus) {
		 ReviewStatusType rt = ReviewStatusType.forName(newStatus);
		 boolean res = ratingReviewService.changeReviewDetailStatus(UUID.fromString(id), rt);
		 if (res) return newStatus;
		 else return "error";
	}

}
