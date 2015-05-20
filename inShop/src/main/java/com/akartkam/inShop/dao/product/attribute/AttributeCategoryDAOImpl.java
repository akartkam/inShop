package com.akartkam.inShop.dao.product.attribute;


import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;

@Repository
public class AttributeCategoryDAOImpl extends AbstractGenericDAO<AttributeCategory> implements
		AttributeCategoryDAO {

	@Override
	public AttributeCategory findAttributeCategoryByName(String name) {
		Criteria criteria = currentSession().createCriteria(AttributeCategory.class)
				.add(Restrictions.eq("name", name));
		criteria.setCacheable(true);
		criteria.setCacheRegion("query.Catalog");
        return (AttributeCategory)criteria.uniqueResult();
	}

}
