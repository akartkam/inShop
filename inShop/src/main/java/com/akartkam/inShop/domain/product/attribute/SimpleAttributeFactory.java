package com.akartkam.inShop.domain.product.attribute;

public class SimpleAttributeFactory {
	
	@SuppressWarnings("rawtypes")
	public static AbstractAttribute createAttribute(AttributeType attributeType) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class cl = Class.forName(attributeType.getClassAttributeName());
		return (AbstractAttribute) cl.newInstance();
	}

	@SuppressWarnings("rawtypes")
	public static AbstractAttributeValue createAttributeValue(AttributeType attributeType) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class cl = Class.forName(attributeType.getClassAttributeValueName());
		return (AbstractAttributeValue) cl.newInstance();
	}
	
}
