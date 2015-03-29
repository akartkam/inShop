package com.akartkam.inShop.service.product;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;

public interface CategoryService {
	/*
	 * List<Category> getAllCategories();
	 */
	Category createCategory(Category category);
	List<Category> getRootCategories(Boolean useDisabled);
	List<Category> getCategoryByName(String name);
	List<Product> getProductByName(String name);
	List<Category> getAllCategoryHierarchy();
	Category loadCategoryById(UUID id, Boolean lock);
	Category getCategoryById(UUID id);
	void updateCategory(Category category);
	void softDeleteCategoryById(UUID id);
	void mergeWithExistingAndUpdateOrCreate(final Category categoryFromPost);
	Category cloneCategoryById(UUID id) throws CloneNotSupportedException;
}
