package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.product.Category;

public class CategoryDAOImpl extends AbstractGenericDAO<Category> implements
		CategoryDAO {


	@Override
	public List<Category> readAllCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> readAllParentCategories() {
		// TODO Auto-generated method stub
		return null;
	}

}
