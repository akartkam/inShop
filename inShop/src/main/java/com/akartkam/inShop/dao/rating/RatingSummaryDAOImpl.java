package com.akartkam.inShop.dao.rating;

import javax.persistence.NoResultException;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.rating.RatingSummary;
import com.akartkam.inShop.domain.rating.RatingType;

@Repository
public class RatingSummaryDAOImpl extends AbstractGenericDAO<RatingSummary> implements RatingSummaryDAO {

	@Override
	public RatingSummary readRatingSummary(String itemId, RatingType type) {
		Query q = currentSession().getNamedQuery("findRatingSummary");
		q.setString("itemId", itemId);
		q.setString("ratingType", type.toString());
        RatingSummary ratingSummary = null;
        try {
            ratingSummary = (RatingSummary) q.uniqueResult();
        } catch (NoResultException e) {
            // ignore
        }		
		return ratingSummary;
	}

}
