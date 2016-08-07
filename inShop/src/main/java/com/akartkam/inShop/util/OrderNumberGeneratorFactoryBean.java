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
			return new DefaultOrderNumberGeneratorImpl(prefix);
		}
	}

}
