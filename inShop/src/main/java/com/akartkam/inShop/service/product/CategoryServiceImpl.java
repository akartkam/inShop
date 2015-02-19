package com.akartkam.inShop.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.product.CategoryDAO;
import com.akartkam.inShop.dao.product.ProductDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;

@Service("CategoryService")
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private ProductDAO productDAO;

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

	@Override
	public List<Product> getProductByName(String name) {
		return productDAO.findProductByName(name);
	}

}
