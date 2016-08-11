package com.akartkam.inShop.util;


import java.util.Properties;

import org.springframework.beans.factory.config.AbstractFactoryBean;

public class OrderNumberGeneratorFactoryBean extends AbstractFactoryBean<OrderNumberGenerator> {

	private Properties parameters;
	
	public Properties getParameters() {
		return parameters;
	}

	public void setParameters(Properties parameters) {
		this.parameters = parameters;
	}

	@Override
	public Class<?> getObjectType() {
		return OrderNumberGenerator.class;
	}

	@Override
	protected OrderNumberGenerator createInstance() throws Exception { 
		String prefix = parameters.getProperty("orderNumberPrefix");
		if (parameters.containsKey("useAutogenerateOrderNumber") && Boolean.parseBoolean(parameters.getProperty("useAutogenerateOrderNumber"))) {
			return new Random10OrderNumberGeneratorImpl(prefix);
		} else {
			String sql = parameters.getProperty("sql");
			if (sql == null || "".equals(sql)) throw new IllegalStateException("sql parameter is null for DefaultOrderNumberGeneratorImpl");
			return new DefaultOrderNumberGeneratorImpl(prefix, sql);
		}
	}

}
