package com.akartkam.inShop.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.akartkam.inShop.common.AbstractTest;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.util.Constants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProductSkuServiceTest extends AbstractTest {
	private static final Log LOG = LogFactory.getLog(ProductSkuServiceTest.class);
	
	@Autowired
	private ProductService  productService;
	@Autowired
	private SessionFactory sessionFactory;
	

	private List<String> ids = new ArrayList<String>();
	
	@Before
	public void before() {
		ids.add("4c080065-250d-4386-876c-fc0bf6c71e52");
		//ids.add("4ea061cf-dbaf-485b-873d-c92924e3c4f2");
		//ids.add("952e14b6-e0d8-4606-897d-a310b677b20f");
		//ids.add("d6120190-1591-4207-868c-853aea97efa1");
		//ids.add("c640d72a-f94d-46cb-8cf8-9242e04e6813");		
	}
	

	@Test
	public void getSkusByListOfIds_criteria() {
	
		/*
		Query query = sessionFactory.getCurrentSession().
				   createQuery("from Sku where id in :ids");
		query.setParameterList("ids", ids);
		List res = query.list();
		*/
		List<Sku> res = sessionFactory.getCurrentSession().createCriteria(Sku.class)
				        .add(Restrictions.in("id", ids))
				        .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
				        .list();
		LOG.info(res);
	}

	@Test
	public void getSkusByListOfIds_sql() {
		LOG.info("executing getSkusByListOfIds_sql");
		List<?> res = sessionFactory.getCurrentSession().createSQLQuery(Constants.SELECT_SKU_MAP_ID_QUANTITY_AVAILABLE)
						.addScalar("id", StringType.INSTANCE)
						.addScalar("quantity_avable", IntegerType.INSTANCE)
				        .setParameterList("ids", ids)
				        .list();
		Object[] obj = null;
		Map<UUID, Integer> resMap = new HashMap<UUID, Integer>();
		for (Iterator<?> iter = res.iterator(); iter.hasNext();) {
			obj = (Object[]) iter.next();
			resMap.put(UUID.fromString((String)obj[0]), (Integer)obj[1]);
		}
		LOG.info(resMap);
		
	}
	
	
}
