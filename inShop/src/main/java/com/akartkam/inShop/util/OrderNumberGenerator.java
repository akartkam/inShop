package com.akartkam.inShop.util;


public interface OrderNumberGenerator {
	void setPrefix(String prefix);
	String generateOrderNumber();
}
