package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.product.Category;

public interface CategoryDAO extends GenericDAO<Category, UUID> {
	List<Category> readAllCategories();
	List<Category> readAllSubCategories(@NotNull Category category);

}
