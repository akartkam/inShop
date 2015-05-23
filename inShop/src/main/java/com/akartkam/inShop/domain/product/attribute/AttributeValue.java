package com.akartkam.inShop.domain.product.attribute;



public interface AttributeValue<T> {
	T getValue();
	void setValue(T value);
	AbstractAttribute getAttribute();
}
