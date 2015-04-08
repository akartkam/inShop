package com.akartkam.inShop.service.product;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.product.attribute.AttributeCategory;

public interface AttributeCategoryService {
	/*
	 * List<Category> getAllCategories();
	 */
	AttributeCategory createAttributeCategory(AttributeCategory category);
	List<AttributeCategory> getAllAttributeCategory();
	List<AttributeCategory> getAttributeCategoryByName(String name);
	AttributeCategory loadAttributeCategoryById(UUID id, Boolean lock);
	AttributeCategory getAttributeCategoryById(UUID id);
	void updateAttributeCategory(AttributeCategory category);
	void softDeleteAttributeCategoryById(UUID id);
	void mergeWithExistingAndUpdateOrCreate(final AttributeCategory categoryFromPost);
	@SuppressWarnings("rawtypes")
	List buildAttributeCategoryHierarchy();	
}
