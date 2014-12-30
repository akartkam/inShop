package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.UUID;


import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.product.Category;


public interface CategoryDAO extends GenericDAO<Category, UUID> {
	/* Добавил метон list в abstractdao 
	 * List<Category> readAllCategories();
	 */
	List<Category> readAllParentCategories();
}
