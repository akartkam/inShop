package com.akartkam.inShop.dao.product.attribute;

import java.util.UUID;

import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;


public interface AttributeValueDAO extends GenericDAO<AbstractAttributeValue, UUID> {
	
	boolean isExistsAttributeValues(AbstractAttribute at, Category category); 

}
