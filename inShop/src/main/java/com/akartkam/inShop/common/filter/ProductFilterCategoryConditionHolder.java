package com.akartkam.inShop.common.filter;

import org.hibernate.Query;
import org.hibernate.Session;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.formbean.ProductFilterDTO;

public class ProductFilterCategoryConditionHolder implements
		ProductFilterConditionHolder {
	
	private Category category;

	public Category getCategory() {
		return category;
	}
	
	public ProductFilterCategoryConditionHolder (Category category) {
		this.category = category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public Query getInitializedQueryForFilterDTO(Session session) {
		Query query = session.getNamedQuery("findFilteredProductByCategory").setString("c", category.getId().toString());
		return query;
	}

	@Override
	public void adjustFilterDTO(ProductFilterDTO filterDTO) {
		filterDTO.setCategoryId(category.getId());
		
	}

	@Override
	public Query getInitializedQueryForFilteredProduct(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

}
