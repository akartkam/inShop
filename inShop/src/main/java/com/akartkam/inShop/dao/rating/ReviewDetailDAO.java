package com.akartkam.inShop.dao.rating;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.rating.RatingType;
import com.akartkam.inShop.domain.rating.ReviewDetail;

public interface ReviewDetailDAO extends GenericDAO<ReviewDetail, UUID> {
	List<ReviewDetail> findCommonReviewDetails();
	List<ReviewDetail> findReviewDetails(String itemId, RatingType ratingType);
}
