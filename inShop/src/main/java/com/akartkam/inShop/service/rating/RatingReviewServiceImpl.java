package com.akartkam.inShop.service.rating;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.domain.rating.ReviewDetail;

@Service("RatingReviewService")
@Transactional(readOnly = true)
public class RatingReviewServiceImpl implements RatingReviewService {
	
	//@Autowired
	//private 

	@Override
	@Transactional(readOnly = false)
	public void createCommonReview(ReviewDetail reviewDetail) {
		
	}

}
