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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.util.Constants;

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
	public Map<Long, List<Sku>> findSkusByCodeOrNameForPaging(String s, int rowPerPage, int pageNumber) {
		Map<Long, List<Sku>> res = new HashMap<Long, List<Sku>>();
		Criteria criteriaCount = currentSession().createCriteria(Sku.class)
				.add(Restrictions.or(Restrictions.ilike("name", s), Restrictions.ilike("code", s)))
				.add(Restrictions.eq("enabled", new Boolean(true)))
				.setProjection(Projections.rowCount());
		Long totalRowCount = (Long) criteriaCount.uniqueResult();
		int offset = (pageNumber - 1) * rowPerPage;
		Criteria criteria = currentSession().createCriteria(Sku.class)
				.add(Restrictions.or(Restrictions.ilike("name", s), Restrictions.ilike("code", s)))
				.add(Restrictions.eq("enabled", new Boolean(true)))
				.addOrder(Order.asc("ordering"));
		criteria.setFirstResult(offset);
		criteria.setMaxResults(rowPerPage);		
		criteria.setCacheable(true);
		criteria.setCacheRegion("query.Catalog");
		res.put(totalRowCount, criteria.list());
		return null;
	}	

	@Override
	public Map<UUID, Integer> findMapSkuIdQuantityAvailable(Collection<Sku> skus) {
		List<String> ids = new ArrayList<String>();
		Object[] obj = null;
		Map<UUID, Integer> resMap = new HashMap<UUID, Integer>();
		for (Sku sku : skus) ids.add(sku.getId().toString());
		List<?> res = currentSession()
				        .createSQLQuery(Constants.SELECT_SKU_MAP_ID_QUANTITY_AVAILABLE)
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
	
	@Override
	public void delete(Sku object) {
		currentSession().delete(object);
		/*getSessionFactory().getCache().evictQueryRegions();
		getSessionFactory().getCache().evictEntityRegion(Sku.class);*/
	}

 
}
