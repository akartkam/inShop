package com.akartkam.inShop.dao.rating;

import java.util.UUID;

import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.rating.RatingSummary;
import com.akartkam.inShop.domain.rating.RatingType;

public interface RatingSummaryDAO extends GenericDAO<RatingSummary, UUID> {
	RatingSummary readRatingSummary(final String itemId, final RatingType type);
}
