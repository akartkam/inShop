package com.akartkam.inShop.service.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.rating.RatingSummaryDAO;
import com.akartkam.inShop.domain.rating.RatingSummary;
import com.akartkam.inShop.domain.rating.RatingType;
import com.akartkam.inShop.domain.rating.ReviewDetail;

@Service("RatingReviewService")
@Transactional(readOnly = true)
public class RatingReviewServiceImpl implements RatingReviewService {
	
	@Autowired
	private RatingSummaryDAO ratingSummaryDAO;


	@Override
	@Transactional(readOnly = false)
	public void createRatingReview(ReviewDetail reviewDetail, String itemId, RatingType ratingType) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public RatingSummary readRatingSummary(String itemId, RatingType type) {
		// TODO Auto-generated method stub
		return null;
	}

}
