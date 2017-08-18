package com.akartkam.inShop.domain.rating;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;

import com.akartkam.inShop.domain.AbstractDomainObject;
import com.akartkam.inShop.domain.Instruction;
import com.akartkam.inShop.domain.customer.Customer;

@NamedNativeQueries({
@NamedNativeQuery(
		name = "findCommonReviewDetails",
		query = "select rd.* from review_detail rd, rating_summary rs " + 
				" where rs.rating_type='COMMON' and " +
				" rs.id=rd.rating_summary_id and " +
                " rd.review_status='APPROVED' ", resultClass=ReviewDetail.class),  
})


@Entity
@Table(name = "Review_detail")
public class ReviewDetail extends AbstractDomainObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3043730158069062620L;
	private Double rating;
	private String name;
	private String email;
    private Customer customer;
    private DateTime reivewSubmittedDate;
    private String reviewText;
    private ReviewStatusType reviewStatus;
    private Integer helpfulCount;
    private Integer notHelpfulCount;
    private RatingSummary ratingSummary;

    @Column(name = "RATING")
    public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}    
    
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    @Index(name="REVIEWDETAIL_CUSTOMER_INDEX", columnNames={"CUSTOMER_ID"})    
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

    @Column(name = "REVIEW_SUBMITTED_DATE", nullable = false)
	public DateTime getReivewSubmittedDate() {
		return reivewSubmittedDate;
	}

	public void setReivewSubmittedDate(DateTime reivewSubmittedDate) {
		this.reivewSubmittedDate = reivewSubmittedDate;
	}

    @Column(name = "REVIEW_TEXT", nullable = false)
	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

    @Column(name = "REVIEW_STATUS")
    @Index(name="REVIEWDETAIL_STATUS_INDEX", columnNames={"REVIEW_STATUS"})
    @Enumerated(EnumType.STRING)
	public ReviewStatusType getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(ReviewStatusType reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

    @Column(name = "HELPFUL_COUNT")
	public Integer getHelpfulCount() {
		return helpfulCount;
	}

	public void setHelpfulCount(Integer helpfulCount) {
		this.helpfulCount = helpfulCount;
	}

	@Column(name = "NOT_HELPFUL_COUNT")
	public Integer getNotHelpfulCount() {
		return notHelpfulCount;
	}

	public void setNotHelpfulCount(Integer notHelpfulCount) {
		this.notHelpfulCount = notHelpfulCount;
	}

    @ManyToOne(optional = false)
    @JoinColumn(name = "RATING_SUMMARY_ID")
    @Index(name="REVIEWDETAIL_SUMMARY_INDEX", columnNames={"RATING_SUMMARY_ID"})
	public RatingSummary getRatingSummary() {
		return ratingSummary;
	}

	public void setRatingSummary(RatingSummary ratingSummary) {
		this.ratingSummary = ratingSummary;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Email
	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
    

}
