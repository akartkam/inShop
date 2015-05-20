package com.akartkam.inShop.dao.product.attribute;

import java.util.UUID;





import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;


public interface AttributeCategoryDAO extends GenericDAO<AttributeCategory, UUID> {
	AttributeCategory findAttributeCategoryByName(String name);
}
