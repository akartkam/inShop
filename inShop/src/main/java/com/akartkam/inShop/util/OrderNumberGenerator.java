package com.akartkam.inShop.util;

import org.springframework.jdbc.core.JdbcTemplate;

public interface OrderNumberGenerator {
	void setPrefix(String prefix);
	String generateOrderNumber(JdbcTemplate jdbcTemplate);
}
