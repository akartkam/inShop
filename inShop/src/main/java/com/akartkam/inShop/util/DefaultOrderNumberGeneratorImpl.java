package com.akartkam.inShop.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class DefaultOrderNumberGeneratorImpl implements OrderNumberGenerator {
	private static final Log LOG = LogFactory.getLog(DefaultOrderNumberGeneratorImpl.class);
	protected String prefix; 
	
	private static final String SELECT_ORDER_NUMBER_GENERATOR =
			"SELECT nextval ('order_number_generator') as nextval";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public DefaultOrderNumberGeneratorImpl() {		 
	}
	
	public DefaultOrderNumberGeneratorImpl (String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public String generateOrderNumber() {		
		 Long n = jdbcTemplate.queryForObject(SELECT_ORDER_NUMBER_GENERATOR, Long.class);
		 String on = prefix + String.format("%d", n);
		 LOG.debug("Generated Order Number: " + on);
		 return on;
	}

}
