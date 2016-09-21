package com.akartkam.inShop.dao.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
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
        return skus; 	
	}

	@Override
	public Map<UUID, Integer> findMapSkuIdQuantityAvailable(Collection<Sku> skus) {
		List<UUID> ids = new ArrayList<UUID>();
		Object[] obj = null;
		Map<UUID, Integer> resMap = new HashMap<UUID, Integer>();
		for (Sku sku : skus) ids.add(sku.getId());
		List<?> res = currentSession()
				        .createSQLQuery("SELECT id, quantity_avable FROM sku WHERE id in :ids")
						.addScalar("id", StringType.INSTANCE)
						.addScalar("quantity_avable", IntegerType.INSTANCE)
				        .setParameterList("ids", ids)
				        .list();
		for (Iterator<?> iter = res.iterator(); iter.hasNext();) {
			obj = (Object[]) iter.next();
			resMap.put(UUID.fromString((String)obj[0]), (Integer)obj[1]);
		}
		return resMap;
	}
}
