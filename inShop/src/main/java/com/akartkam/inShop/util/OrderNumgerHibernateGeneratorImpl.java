package com.akartkam.inShop.util;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class OrderNumgerHibernateGeneratorImpl implements IdentifierGenerator {

	@Autowired
	private ApplicationContext appContext;
	
	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {		
		OrderNumberGenerator onGen = (OrderNumberGenerator)appContext.getBean("orderNumberGenerator");
		String on = onGen.generateOrderNumber(session);		
		return on;
	}

}
