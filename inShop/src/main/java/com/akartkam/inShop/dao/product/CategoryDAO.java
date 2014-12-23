package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;

public interface CategoryDAO extends GenericDAO<Category, UUID> {
	List<Category> readAllCategories();
	List<Category> readAllSubCategories(@NotNull Category category);
	List<Product> readAllProductsByCategory(@NotNull Category category); 

}
