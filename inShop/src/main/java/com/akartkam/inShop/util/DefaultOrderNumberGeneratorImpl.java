package com.akartkam.inShop.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

public class DefaultOrderNumberGeneratorImpl implements OrderNumberGenerator {
	private static final Log LOG = LogFactory.getLog(DefaultOrderNumberGeneratorImpl.class);
	protected String prefix; 
	
	@Value("T(com.akartkam.inShop.util.Сonstants).SELECT_ORDER_NUMBER_GENERATOR_TEST")
	private String sql; 
	
	
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
	public String generateOrderNumber(JdbcTemplate jdbcTemplate) {		
		 Long n = jdbcTemplate.queryForObject(sql, Long.class);
		 String on = prefix + String.format("%d", n);
		 LOG.debug("Generated Order Number: " + on);
		 return on;
	}

}
