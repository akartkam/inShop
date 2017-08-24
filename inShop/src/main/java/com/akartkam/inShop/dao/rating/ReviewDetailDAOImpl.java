package com.akartkam.inShop.dao.rating;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.rating.RatingType;
import com.akartkam.inShop.domain.rating.ReviewDetail;

@Repository
public class ReviewDetailDAOImpl extends AbstractGenericDAO<ReviewDetail> implements ReviewDetailDAO {

	@Override
	public List<ReviewDetail> findCommonReviewDetails() {
		Query q = currentSession().getNamedQuery("findCommonReviewDetails");
		return (List<ReviewDetail>) q.list();
	}

	@Override
	public List<ReviewDetail> findReviewDetails(String itemId, RatingType ratingType) {
		Query q = currentSession().getNamedQuery("findReviewDetails");
		q.setString("item_id", itemId);
		q.setString("rating_type", ratingType.toString());
		return (List<ReviewDetail>) q.list();
	}

}
