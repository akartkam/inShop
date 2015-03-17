package com.akartkam.inShop.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

	@Override
	public List<Category> getAllCategoryHierarchy() {
		List<Category> allCategoryHierarchy = new ArrayList<Category>();
		for(Category rct: getRootCategories()) {
			rct.buildSubCategoryHierarchy(allCategoryHierarchy);
		}
		return allCategoryHierarchy;
	}

	@Override
	public Category getCategoryById(String id) {
		return categoryDAO.get(UUID.fromString(id));
	}

	@Override
	public Category getCategoryById(UUID id) {
		return categoryDAO.get(id);
	}	
	
	@Override
	@Transactional(readOnly = false)
	public void updateCategory(Category category) {
		categoryDAO.update(category);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdate(final Category categoryFromPost) {

        UUID id = categoryFromPost.getId();
		final Category existingCategory = getCategoryById(id);

        // set here explicitly what must/can be overwritten by the html form POST
        existingCategory.setName(categoryFromPost.getName());
        existingCategory.setParent(categoryFromPost.getParent());
        existingCategory.setDescription(categoryFromPost.getDescription());
        existingCategory.setLongDescription(categoryFromPost.getLongDescription());
        existingCategory.setEnabled(categoryFromPost.isEnabled());
        updateCategory(existingCategory);
    }

}
