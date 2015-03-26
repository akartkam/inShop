package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.UUID;





import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;


public interface AttributeCategoryDAO extends GenericDAO<AttributeCategory, UUID> {
	List<AttributeCategory> findAttributeCategoryByName(String name);
}
