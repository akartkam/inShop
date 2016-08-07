package com.akartkam.inShop.util;

import org.hibernate.engine.spi.SessionImplementor;

public interface OrderNumberGenerator {
	void setPrefix(String prefix);
	String generateOrderNumber(SessionImplementor session);
}
