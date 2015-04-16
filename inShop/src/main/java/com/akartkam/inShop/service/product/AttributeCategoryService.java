package com.akartkam.inShop.service.product;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;
import com.akartkam.inShop.formbean.AttributeForm;

public interface AttributeCategoryService {
	/*
	 * List<Category> getAllCategories();
	 */
	AttributeCategory createAttributeCategory(AttributeCategory category);
	List<AttributeCategory> getAllAttributeCategory();
	List<AttributeCategory> getAttributeCategoryByName(String name);
	AttributeCategory loadAttributeCategoryById(UUID id, Boolean lock);
	AbstractAttribute loadAttributeById(UUID id, Boolean lock);
	AttributeCategory getAttributeCategoryById(UUID id);
	void updateAttributeCategory(AttributeCategory category);
	void updateAttribute(AbstractAttribute attribute);
	void deleteAttributeCategoryById(UUID id);
	void deleteAttributeById(UUID id);
	void deleteAttribute(AbstractAttribute attribute);
	void deleteAttributeCategory(AttributeCategory category);
	void softDeleteAttributeCategoryById(UUID id);
	void softDeleteAttributeById(UUID id);
	void mergeWithExistingAndUpdateOrCreate(final AttributeCategory categoryFromPost) ;
	void mergeWithExistingAndUpdateOrCreate(final AttributeForm attributeFromPost) throws ClassNotFoundException, InstantiationException, IllegalAccessException; 
	@SuppressWarnings("rawtypes")
	List buildAttributeCategoryHierarchy();	
	AbstractAttribute getAttributeById(UUID id);
	AttributeCategory cloneAttributeCategoryById(UUID id) throws CloneNotSupportedException;
	AbstractAttribute cloneAttributeById(UUID id) throws CloneNotSupportedException;
}
