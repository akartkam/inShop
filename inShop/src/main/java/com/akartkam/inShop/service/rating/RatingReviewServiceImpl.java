package com.akartkam.inShop.service.rating;

import java.util.List;

import org.joda.time.DateTime;
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
			rs = ratingSummaryDAO.create(rs);
		}
		reviewDetail.setRatingSummary(rs);
		reviewDetail.setReivewSubmittedDate(new DateTime());
		rs.getReviews().add(reviewDetail);	
		reviewDetailDAO.create(reviewDetail);
	}


	@Override
	public RatingSummary readRatingSummary(String itemId, RatingType type) {
		return ratingSummaryDAO.readRatingSummary(itemId, type) ;
	}


	@Override
	public List<ReviewDetail> readReviewDetails(String itemId, RatingType ratingType) {
		return reviewDetailDAO.findReviewDetails(itemId, ratingType);
	}


	@Override
	public List<ReviewDetail> readCommonReviewDetails() {
		return reviewDetailDAO.findCommonReviewDetails();
	}

}
