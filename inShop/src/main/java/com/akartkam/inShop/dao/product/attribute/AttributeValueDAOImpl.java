package com.akartkam.inShop.dao.product.attribute;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;

@SuppressWarnings("rawtypes")
@Repository
public class AttributeValueDAOImpl extends
		AbstractGenericDAO<AbstractAttributeValue> implements AttributeValueDAO {
    private final String IS_EXISTS_ATTR_VALUES_BY_CATEGORY = "select count(av.id) from AbstractAttributeValue av where category = :category and attribute = :attribute"; 
	
	
	@Override
	public boolean isExistsAttributeValues(AbstractAttribute at, Category category) {

		Query q = currentSession().createQuery(IS_EXISTS_ATTR_VALUES_BY_CATEGORY);
		q.setParameter("category", category);
		q.setParameter("attribute", at);
		return (Long)q.uniqueResult() != 0;
	}

}
