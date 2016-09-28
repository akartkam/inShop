package com.akartkam.inShop.util;

public interface Constants {
	/*SQL*/
	public static final String SELECT_ORDER_NUMBER_GENERATOR_HSQL = "CALL NEXT VALUE FOR order_number_generator;";
	public static final String SELECT_ORDER_NUMBER_GENERATOR_POSTGRES = "SELECT nextval ('order_number_generator') as nextval;";
	public static final String SELECT_SKU_MAP_ID_QUANTITY_AVAILABLE = "SELECT id, quantity_avable FROM sku WHERE CAST(id AS VARCHAR(36)) in :ids";
	public static final String SELECT_ORDERITEM_MAP_ID_QUANTITY = "SELECT id, quantity FROM customer_order_item WHERE CAST(id AS VARCHAR(36)) in :ids";
	/*Other const*/
	public static final String CART_BEAN_NAME = "curCart";
}
