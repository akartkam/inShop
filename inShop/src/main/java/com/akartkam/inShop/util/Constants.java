package com.akartkam.inShop.util;

public interface Constants {
	public static final String SELECT_ORDER_NUMBER_GENERATOR_HSQL = "SELECT NEXT VALUE FOR order_number_generator";
	public static final String SELECT_ORDER_NUMBER_GENERATOR_POSTGRES = "SELECT nextval ('order_number_generator') as nextval";	
}
