package com.akartkam.inShop.dao.product;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.product.Sku;

@Repository
public class SkuDAOImpl extends AbstractGenericDAO<Sku> implements SkuDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Sku> findSkusByName(String name){ 
		Criteria criteria = currentSession().createCriteria(Sku.class)
				.add(Restrictions.ilike("name", name))
				.add(Restrictions.eq("enabled", new Boolean(true)))
				.addOrder(Order.asc("ordering"));
		criteria.setCacheable(true);
		criteria.setCacheRegion("query.Catalog");
		List<Sku> skus = criteria.list();
		CollectionUtils.filter(skus, new Predicate<Sku>() {
			@Override
			public boolean evaluate(Sku object) {
				boolean ret = object.getDefaultProduct() != null ? object.getDefaultProduct().isCanSellWithoutOptions() : true;
				return ret;
			}
			
		});
        return skus; 	
	}
}
