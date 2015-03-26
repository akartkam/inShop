package com.akartkam.inShop.service.product;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.product.AttributeCategoryDAO;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;


@Service("AttributeCategoryService")
@Transactional(readOnly = true)
public class AttributeCategoryServiceImpl implements AttributeCategoryService {
	
	@Autowired
	private AttributeCategoryDAO attributeCategoryDAO;


	@Override
	@Transactional(readOnly = false)
	public AttributeCategory createAttributeCategory(AttributeCategory category) {
		return	attributeCategoryDAO.create(category);
	}


	@Override
	public List<AttributeCategory> getAttributeCategoryByName(String name) {
		return attributeCategoryDAO.findAttributeCategoryByName(name);
	}


	@Override
	public AttributeCategory getAttributeCategoryById(UUID id) {
		return attributeCategoryDAO.get(id);
	}	
	
	@Override
	@Transactional(readOnly = false)
	public void updateAttributeCategory(AttributeCategory category) {
		attributeCategoryDAO.update(category);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(final AttributeCategory categoryFromPost) {
		if (categoryFromPost == null) return;
		final AttributeCategory existingCategory = getAttributeCategoryById(categoryFromPost.getId());
		if (existingCategory != null) {
	        // set here explicitly what must/can be overwritten by the html form POST
	        existingCategory.setName(categoryFromPost.getName());
	        existingCategory.setEnabled(categoryFromPost.isEnabled());
	        updateAttributeCategory(existingCategory);
		} else {
			createAttributeCategory(categoryFromPost);
		}
    }

	@Override
	@Transactional(readOnly = false)
	public void softDeleteAttributeCategoryById(UUID id) {
		AttributeCategory category = getAttributeCategoryById(id);
		if (category != null) {
			category.setEnabled(false);
			updateAttributeCategory(category);
		}
		
	}

	@Override
	public AttributeCategory loadAttributeCategoryById(UUID id, Boolean lock) {
		return attributeCategoryDAO.findById(id, lock);
	}


	@Override
	public List<AttributeCategory> getAllAttributeCategory() {
		return attributeCategoryDAO.list();
	}

}
