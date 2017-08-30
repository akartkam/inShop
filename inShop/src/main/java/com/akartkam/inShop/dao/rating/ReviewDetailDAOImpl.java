package com.akartkam.inShop.dao.rating;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.Instruction;
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

	@Override
	public List<Object[]> findReviewDetailsForAdmin() {
		Query q = currentSession().createSQLQuery("select {rd.*}, "+
	  			"       case when rs.rating_type='PRODUCT' then s.name else null end as product_name, "+
	  			"       case when rs.rating_type='PRODUCT' then s.code else null end as product_code, "+
	  			"       case when rs.rating_type='PRODUCT' then p.url else null end as product_url "+
	  			"  from review_detail {rd} "+
	  			"       left join rating_summary rs on rs.id=rd.rating_summary_id "+
	  			"       left join product p on cast(p.id as varchar)=rs.item_id"+
	  			"       left join sku s on s.id=p.default_sku_id "+
	  			"  order by rd.review_submitted_date desc")
  	      .addEntity("rd", ReviewDetail.class)
          .addScalar("product_name", StandardBasicTypes.STRING)
          .addScalar("product_code", StandardBasicTypes.STRING)
          .addScalar("product_url", StandardBasicTypes.STRING);
		return  q.list();
	}

}
