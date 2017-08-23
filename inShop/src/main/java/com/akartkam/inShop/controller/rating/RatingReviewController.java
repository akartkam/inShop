package com.akartkam.inShop.controller.rating;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.akartkam.inShop.domain.rating.RatingType;
import com.akartkam.inShop.domain.rating.ReviewDetail;
import com.akartkam.inShop.exception.AddToCartException;
import com.akartkam.inShop.exception.InventoryUnavailableException;
import com.akartkam.inShop.exception.ProductNotFoundException;
import com.akartkam.inShop.exception.RequiredAttributeNotProvidedException;
import com.akartkam.inShop.exception.SkuNotFoundException;
import com.akartkam.inShop.formbean.Buy1clickForm;
import com.akartkam.inShop.formbean.CartItemForm;
import com.akartkam.inShop.service.rating.RatingReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/rating")
public class RatingReviewController {
	private static final Log LOG = LogFactory.getLog(RatingReviewController.class);
	
	@Autowired
	private RatingReviewService ratingReviewService;
	
	@RequestMapping(value = "/add-review")
    public String showAddReview(@RequestParam(value = "item-id") String itemId,
    							@RequestParam(value = "rating-type") String ratingType,
    						final Model model) throws IOException {
		model.addAttribute("reviewDetail", new ReviewDetail());
    	return "/rating/partials/submitReview :: submitReviewBlock (itemId='"+itemId+"', ratingType='"+ratingType+"')";
	}
		
	@RequestMapping(value = "/submit-review")
    public String submitReview(
				    		@RequestParam(value = "item-id") String itemId,
							@RequestParam(value = "rating-type") String ratingType, 
							@ModelAttribute("reviewDetail") @Valid ReviewDetail reviewDetail,
							final BindingResult bindingResult,							
    						final Model model
    		                 ) throws IOException {
		if (bindingResult.hasErrors()) {
			model.addAttribute("org.springframework.validation.BindingResult.ReviewDetail", bindingResult);
			return "/rating/partials/submitReview :: submitReviewBlock (itemId='"+itemId+"', ratingType='"+ratingType+"')";
    	} else {
    		try {
    			ratingReviewService.createRatingReview(reviewDetail, itemId, RatingType.forName(ratingType));
    		} catch (Exception e) {
    			LOG.error("",e);
    			return "/order/partials/buy1click-fail";
    		}
    		return "/order/partials/buy1click-success";
    	}
	    	
	}
}
 