package com.akartkam.inShop.dao.product;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;

@Repository
public class AttributeCategoryDAOImpl extends AbstractGenericDAO<AttributeCategory> implements
		AttributeCategoryDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<AttributeCategory> findAttributeCategoryByName(String name) {
		Criteria criteria = currentSession().createCriteria(AttributeCategory.class)
				.add(Restrictions.eq("name", name))
				.add(Restrictions.eq("enabled", true))
				.addOrder(Order.asc("ordering"));
		criteria.setCacheable(true);
		criteria.setCacheRegion("query.Catalog");
        return (List<AttributeCategory>) criteria.list();
	}

}
