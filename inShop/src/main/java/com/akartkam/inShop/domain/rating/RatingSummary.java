package com.akartkam.inShop.domain.rating;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Index;

import com.akartkam.inShop.domain.AbstractDomainObject;

public class RatingSummary extends AbstractDomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7877228800382279712L;
	private String itemId;
    private Double averageRating = new Double(0);
    protected RatingType ratingType;
    //private List<RatingDetail> ratings = new ArrayList<RatingDetail>();
    //private List<ReviewDetail> reviews = new ArrayList<ReviewDetail>();

    @Column(name = "ITEM_ID")
    @Index(name="ITEM_ID_INDEX", columnNames={"ITEM_ID"})
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

    @Column(name = "AVERAGE_RATING", nullable = false)
	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}

    /*@OneToMany(mappedBy = "ratingSummary", cascade = {CascadeType.ALL})
	public List<RatingDetail> getRatings() {
		return ratings;
	}

	public void setRatings(List<RatingDetail> ratings) {
		this.ratings = ratings;
	}

    @OneToMany(mappedBy = "ratingSummary", cascade = {CascadeType.ALL})
	public List<ReviewDetail> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewDetail> reviews) {
		this.reviews = reviews;
	}*/

    @Column(name = "RATING_TYPE")
    @Index(name="RATINGSUMM_TYPE_INDEX", columnNames={"RATING_TYPE"})
    @Enumerated(EnumType.STRING)
	public RatingType getRatingType() {
		return ratingType;
	}

	public void setRatingType(RatingType ratingType) {
		this.ratingType = ratingType;
	}
    
    
    
}
