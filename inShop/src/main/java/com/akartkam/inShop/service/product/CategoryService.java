package com.akartkam.inShop.service.product;

import java.util.List;

import com.akartkam.inShop.domain.product.Category;

public interface CategoryService {
	/*
	 * List<Category> getAllCategories();
	 */
	Category createCategory(Category category);
	List<Category> getRootCategories();
	List<Category> getCategoryByName(String name);
}
