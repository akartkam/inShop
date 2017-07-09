package com.akartkam.inShop.common.filter;

import org.hibernate.Query;
import org.hibernate.Session;

import com.akartkam.inShop.formbean.ProductFilterDTO;

public interface ProductFilterConditionHolder {
	Query getInitializedQueryForFilterDTO(Session session);
	Query getInitializedQueryForFilteredProduct(ProductFilterDTO productFilterDTO, Session session);
	void adjustFilterDTO(ProductFilterDTO filterDTO);

}
