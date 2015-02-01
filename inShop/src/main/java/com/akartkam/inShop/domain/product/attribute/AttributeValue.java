package com.akartkam.inShop.domain.product.attribute;



public interface AttributeValue<T> {
	T getAttributeValue();
	void setAttributeValue(T value);
	AbstractAttribute getAttribute();
}
