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
	List<Category> getRootCategories();
	List<Category> getCategoryByName(String name);
	List<Product> getProductByName(String name);
	List<Category> getAllCategoryHierarchy();
	Category getCategoryById(String id);
	Category getCategoryById(UUID id);
	void updateCategory(Category category);
	public void mergeWithExistingAndUpdate(final Category categoryFromPost);
}
