package com.akartkam.inShop.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.engine.spi.SessionImplementor;

public class Random10OrderNumberGeneratorImpl extends DefaultOrderNumberGeneratorImpl {
	private static final Log LOG = LogFactory.getLog(Random10OrderNumberGeneratorImpl.class);
	
	public Random10OrderNumberGeneratorImpl () {
		
	}
	
	public Random10OrderNumberGeneratorImpl (String prefix) {
		super(prefix);
	}
	
	@Override
	public String generateOrderNumber(SessionImplementor session) {
		Long l = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
		StringBuilder sb =  new StringBuilder();
		if (prefix != null) sb.append(prefix);
		sb.append(String.format("%d", l));
		LOG.debug("Generated Order Number: " + sb.toString());
		return sb.toString();
	}

}
