package com.akartkam.inShop.service.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.product.attribute.AttributeCategoryDAO;
import com.akartkam.inShop.dao.product.attribute.AttributeDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;
import com.akartkam.inShop.domain.product.attribute.SimpleAttributeFactory;


@Service("AttributeCategoryService")
@Transactional(readOnly = true)
public class AttributeCategoryServiceImpl implements AttributeCategoryService {
	
	@Autowired
	private AttributeCategoryDAO attributeCategoryDAO;

	@Autowired
	private AttributeDAO attributeDAO;

	@Override
	@Transactional(readOnly = false)
	public AttributeCategory createAttributeCategory(AttributeCategory category) {
		return	attributeCategoryDAO.create(category);
	}

	@Transactional(readOnly = false)
	public AbstractAttribute createAttribute(AbstractAttribute attribute){
		attributeDAO.create(attribute);
		return	attribute;
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
	        existingCategory.setOrdering(categoryFromPost.getOrdering());
	        existingCategory.setEnabled(categoryFromPost.isEnabled());
	        updateAttributeCategory(existingCategory);
		} else {
			createAttributeCategory(categoryFromPost);
		}
    }

	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(final AbstractAttribute attributeFromPost) 
			   throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (attributeFromPost == null) return;
		final AbstractAttribute existingAttribute = getAttributeById(attributeFromPost.getId());
		if (existingAttribute != null) {
	        // set here explicitly what must/can be overwritten by the html form POST
			existingAttribute.setName(attributeFromPost.getName());
			existingAttribute.setOrdering(attributeFromPost.getOrdering());
			existingAttribute.setEnabled(attributeFromPost.isEnabled());
			AttributeCategory attributeCategoryFromPost = attributeFromPost.getAttributeCategory();
	        if (attributeCategoryFromPost == null) throw new IllegalArgumentException("Attribute Category can not be null!");
	        attributeCategoryFromPost.addAttribute(existingAttribute);
	        updateAttributeCategory(attributeCategoryFromPost);
		} else {
			AbstractAttribute attributeNew = SimpleAttributeFactory.createAttribute(attributeFromPost.getAttribueType());
			attributeNew.setName(attributeFromPost.getName());
			attributeNew.setOrdering(attributeFromPost.getOrdering());
			attributeNew.setEnabled(attributeFromPost.isEnabled());
			AttributeCategory attributeCategoryFromPost = attributeFromPost.getAttributeCategory();
	        if (attributeCategoryFromPost == null) throw new IllegalArgumentException("Attribute Category can not be null!");
	        attributeCategoryFromPost.addAttribute(attributeNew);
	        updateAttributeCategory(attributeCategoryFromPost);
	        //createAttribute(attributeNew);
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List buildAttributeCategoryHierarchy() {
        List currentHierarchy = new ArrayList();
        List<AttributeCategory> attributeCateries = getAllAttributeCategory();
        List<AbstractAttribute> attributes;
        Collections.sort(attributeCateries);
        for (AttributeCategory ctg : attributeCateries) {
        	currentHierarchy.add(ctg);
        	attributes = ctg.getAttributes();
        	Collections.sort(attributes);
        	for (AbstractAttribute attr : attributes) {
        		currentHierarchy.add(attr);
        	}
        }
		return currentHierarchy;
		
	}
	
	@Override
	public AbstractAttribute getAttributeById(UUID id) {
		return attributeDAO.get(id);
	}

}
