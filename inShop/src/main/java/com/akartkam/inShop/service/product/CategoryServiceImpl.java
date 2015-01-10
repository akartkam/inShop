package com.akartkam.inShop.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.product.CategoryDAO;
import com.akartkam.inShop.domain.product.Category;

@Service("CategoryService")
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryDAO categoryDAO;

	@Override
	@Transactional(readOnly = false)
	public Category createCategory(Category category) {
		return	categoryDAO.create(category);
	}

	@Override
	public List<Category> getRootCategories() {
		return categoryDAO.readRootCategories();
	}

	@Override
	public List<Category> getCategoryByName(String name) {
		return categoryDAO.findCategoryByName(name);
	}

}
