package com.akartkam.inShop.service;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.akartkam.inShop.common.AbstractTest;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.service.product.ProductService;

public class SkuControllerTest extends AbstractTest {
	private static final Log LOG = LogFactory.getLog(SkuControllerTest.class);
	
	@Autowired
	private ProductService  productService;
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void getSkusByListOfIds() {
		List ids = new ArrayList();
		ids.add(UUID.fromString("4c080065-250d-4386-876c-fc0bf6c71e52"));
		ids.add(UUID.fromString("4ea061cf-dbaf-485b-873d-c92924e3c4f2"));
		ids.add(UUID.fromString("952e14b6-e0d8-4606-897d-a310b677b20f"));
		ids.add(UUID.fromString("d6120190-1591-4207-868c-853aea97efa1"));
		ids.add(UUID.fromString("c640d72a-f94d-46cb-8cf8-9242e04e6813"));
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

}
