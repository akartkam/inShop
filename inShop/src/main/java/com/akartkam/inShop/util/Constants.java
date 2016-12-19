package com.akartkam.inShop.util;

public interface Constants {
	/*SQL*/
	public static final String SELECT_ORDER_NUMBER_GENERATOR_HSQL = "CALL NEXT VALUE FOR order_number_generator;";
	public static final String SELECT_ORDER_NUMBER_GENERATOR_POSTGRES = "SELECT nextval ('order_number_generator') as nextval;";
	public static final String SELECT_SKU_MAP_ID_QUANTITY_AVAILABLE = "SELECT id, quantity_avable FROM sku WHERE CAST(id AS VARCHAR(36)) in :ids";
	public static final String SELECT_ORDERITEM_MAP_ID_QUANTITY = "SELECT id, quantity FROM customer_order_item WHERE CAST(id AS VARCHAR(36)) in :ids";
	/*Cart const*/
	public static final String CART_BEAN_NAME = "curCart";
	
	/*Classes for web entities*/
	public static String CATEGORY_CLASS = "com.akartkam.inShop.domain.product.Category";
	public static String PRODUCT_CLASS = "com.akartkam.inShop.domain.product.Product";
	public static String BRAND_CLASS = "com.akartkam.inShop.domain.product.Brand";
}
