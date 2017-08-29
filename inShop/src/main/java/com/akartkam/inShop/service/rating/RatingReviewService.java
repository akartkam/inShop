package com.akartkam.inShop.service.rating;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.rating.RatingSummary;
import com.akartkam.inShop.domain.rating.RatingType;
import com.akartkam.inShop.domain.rating.ReviewDetail;
import com.akartkam.inShop.domain.rating.ReviewStatusType;

public interface RatingReviewService {
	RatingSummary readRatingSummary(String itemId, RatingType type);
	void createRatingReview(ReviewDetail reviewDetail, String itemId, RatingType ratingType);
	List<ReviewDetail> readCommonReviewDetails();
	List<ReviewDetail> readReviewDetails(String itemId, RatingType ratingType);
	boolean changeReviewDetailStatus(UUID id, ReviewStatusType status);
	List<ReviewDetail> getAllReviewDetail();
	void deleteReviewDetailById(UUID id);

}
