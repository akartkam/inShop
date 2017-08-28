package com.akartkam.inShop.service.rating;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.rating.RatingSummaryDAO;
import com.akartkam.inShop.dao.rating.ReviewDetailDAO;
import com.akartkam.inShop.domain.rating.RatingSummary;
import com.akartkam.inShop.domain.rating.RatingType;
import com.akartkam.inShop.domain.rating.ReviewDetail;
import com.akartkam.inShop.domain.rating.ReviewStatusType;

@Service("RatingReviewService")
@Transactional(readOnly = true)
public class RatingReviewServiceImpl implements RatingReviewService {
	
	@Autowired
	private RatingSummaryDAO ratingSummaryDAO;
	
	@Autowired
	private ReviewDetailDAO reviewDetailDAO;

	@Value("#{shopProperties['usePremoderateReview']}")
    private Boolean usePremoderateReview;

	@Override
	@Transactional(readOnly = false)
	public void createRatingReview(ReviewDetail reviewDetail, String itemId, RatingType ratingType) {
		RatingSummary rs = readRatingSummary(itemId, ratingType);
		if (rs == null) {
			rs = new RatingSummary();
			rs.setItemId(itemId);
			rs.setRatingType(ratingType);
		}
		reviewDetail.setRatingSummary(rs);
		reviewDetail.setReivewSubmittedDate(new DateTime());
		if (usePremoderateReview) reviewDetail.setReviewStatus(ReviewStatusType.PENDING);
		else reviewDetail.setReviewStatus(ReviewStatusType.APPROVED);
		rs.getReviews().add(reviewDetail);	
		rs.resetAverageRating();
		rs = ratingSummaryDAO.create(rs);

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
