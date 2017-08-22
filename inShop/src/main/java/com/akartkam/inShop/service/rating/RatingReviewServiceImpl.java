package com.akartkam.inShop.service.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.rating.RatingSummaryDAO;
import com.akartkam.inShop.dao.rating.ReviewDetailDAO;
import com.akartkam.inShop.domain.rating.RatingSummary;
import com.akartkam.inShop.domain.rating.RatingType;
import com.akartkam.inShop.domain.rating.ReviewDetail;

@Service("RatingReviewService")
@Transactional(readOnly = true)
public class RatingReviewServiceImpl implements RatingReviewService {
	
	@Autowired
	private RatingSummaryDAO ratingSummaryDAO;
	
	@Autowired
	private ReviewDetailDAO reviewDetailDAO;


	@Override
	@Transactional(readOnly = false)
	public void createRatingReview(ReviewDetail reviewDetail, String itemId, RatingType ratingType) {
		RatingSummary rs = readRatingSummary(itemId, ratingType);
		if (rs == null) {
			rs = new RatingSummary();
			rs.setItemId(itemId);
			rs.setRatingType(ratingType);
			ratingSummaryDAO.create(rs);
		}
		 
	}


	@Override
	public RatingSummary readRatingSummary(String itemId, RatingType type) {
		return ratingSummaryDAO.readRatingSummary(itemId, type) ;
	}

}
