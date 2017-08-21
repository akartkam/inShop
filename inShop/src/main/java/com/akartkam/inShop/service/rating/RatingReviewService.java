package com.akartkam.inShop.service.rating;

import com.akartkam.inShop.domain.rating.RatingSummary;
import com.akartkam.inShop.domain.rating.RatingType;
import com.akartkam.inShop.domain.rating.ReviewDetail;

public interface RatingReviewService {
	RatingSummary readRatingSummary(String itemId, RatingType type);
	void createRatingReview(ReviewDetail reviewDetail, String itemId, RatingType ratingType);

}
