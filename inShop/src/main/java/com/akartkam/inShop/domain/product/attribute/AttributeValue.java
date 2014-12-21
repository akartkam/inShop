package com.akartkam.inShop.domain.product.attribute;

import java.io.Serializable;


public interface AttributeValue {
	Serializable getAttributeValue();
	AbstractAttribute getAttribute();
}
