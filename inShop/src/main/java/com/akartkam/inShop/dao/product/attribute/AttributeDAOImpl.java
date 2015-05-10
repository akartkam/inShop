package com.akartkam.inShop.dao.product.attribute;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;

@Repository
public class AttributeDAOImpl extends AbstractGenericDAO<AbstractAttribute> implements AttributeDAO {

	@SuppressWarnings("unchecked")
	//@Override
	public AbstractAttribute findAttributeByName(String name) {
		Criteria criteria = currentSession().createCriteria(AbstractAttribute.class)
				.add(Restrictions.eq("name", name));
		criteria.setCacheable(true);
		criteria.setCacheRegion("query.Catalog");
        return (AbstractAttribute) criteria.uniqueResult();
	}

}
