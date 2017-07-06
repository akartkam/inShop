package com.akartkam.inShop.common;

import org.hibernate.Query;
import org.hibernate.Session;

public interface ProductFilterConditionHolder {
	Query getInitializedQueryForFilterDTO(Session session);

}
