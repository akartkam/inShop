package com.akartkam.inShop.service.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import com.akartkam.inShop.controller.admin.product.AdminProductController;
import com.akartkam.inShop.dao.product.CategoryDAO;
import com.akartkam.inShop.dao.product.attribute.AttributeCategoryDAO;
import com.akartkam.inShop.dao.product.attribute.AttributeDAO;
import com.akartkam.inShop.dao.product.attribute.AttributeValueDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;
import com.akartkam.inShop.domain.product.attribute.Selectable;
import com.akartkam.inShop.domain.product.attribute.SimpleAttributeFactory;
import com.akartkam.inShop.formbean.AttributeForm;



@Service("AttributeCategoryService")
@Transactional(readOnly = true)
public class AttributeCategoryServiceImpl implements AttributeCategoryService {
	
	private static final Log LOG = LogFactory.getLog(AttributeCategoryServiceImpl.class);
	
	@Autowired
	private AttributeCategoryDAO attributeCategoryDAO;

	@Autowired
	private AttributeDAO attributeDAO;
	
	@Autowired
	private AttributeValueDAO attributeValueDAO;
	
	@Autowired
	private CategoryDAO categoryDAO;
	

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
	public AttributeCategory getAttributeCategoryByName(String name) {
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
	public void updateAttribute(AbstractAttribute attribute) {
		attributeDAO.update(attribute);
	}

	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(final AttributeCategory categoryFromPost, Errors errors) {
		if (categoryFromPost == null) return;
		if (checkAttributeCategory(categoryFromPost, errors)) return;
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

	private boolean checkAttributeCategory (AttributeCategory categoryFromPost, Errors errors) {
		if (categoryFromPost.isNew()) { 
			AttributeCategory cAttr = getAttributeCategoryByName(categoryFromPost.getName());
			if (cAttr != null) {
			  errors.rejectValue("name", "error.duplicate");
		    }
		}
		return errors.hasErrors();	
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(AttributeForm attributeFromPost, Errors errors) 
			   throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (attributeFromPost == null) throw new IllegalArgumentException("Attribute can not be null!");
		final AbstractAttribute existingAttribute = getAttributeById(attributeFromPost.getId());
		if (checkAttribute(existingAttribute, attributeFromPost, errors)) return;
		if (existingAttribute != null) {
	        // set here explicitly what must/can be overwritten by the html form POST
			existingAttribute.setName(attributeFromPost.getName());
			existingAttribute.setCode(attributeFromPost.getCode());
			existingAttribute.setDescription(attributeFromPost.getDescription());
			existingAttribute.setOrdering(attributeFromPost.getOrdering());
			existingAttribute.setEnabled(attributeFromPost.isEnabled());
			existingAttribute.setUnit(attributeFromPost.getUnit());
			existingAttribute.setIsShowOnProductHeader(attributeFromPost.getIsShowOnProductHeader());
			existingAttribute.setAttributeValuesHolder(attributeFromPost.getAttributeValuesHolder());
			if (existingAttribute instanceof Selectable)
				((Selectable)existingAttribute).setStringItems(attributeFromPost.getItems());
			AttributeCategory attributeCategoryFromPost = attributeFromPost.getAttributeCategory();
	        if (attributeCategoryFromPost == null) throw new IllegalArgumentException("Attribute Category can not be null!");
	        attributeCategoryFromPost.addAttribute(existingAttribute);
		} else {
			AbstractAttribute attributeNew = SimpleAttributeFactory.createAttribute(attributeFromPost.getAttributeType());
			attributeNew.setName(attributeFromPost.getName());
			attributeNew.setCode(attributeFromPost.getCode());
			attributeNew.setDescription(attributeFromPost.getDescription());
			attributeNew.setOrdering(attributeFromPost.getOrdering());
			attributeNew.setEnabled(attributeFromPost.isEnabled());
			attributeNew.setUnit(attributeFromPost.getUnit());
			attributeNew.setIsShowOnProductHeader(attributeFromPost.getIsShowOnProductHeader());
			attributeNew.setAttributeValuesHolder(attributeFromPost.getAttributeValuesHolder());
			if (attributeNew instanceof Selectable)
				((Selectable)attributeNew).setStringItems(attributeFromPost.getItems());
			AttributeCategory attributeCategoryFromPost = attributeFromPost.getAttributeCategory();
	        if (attributeCategoryFromPost == null) throw new IllegalArgumentException("Attribute Category can not be null!");
	        attributeCategoryFromPost.addAttribute(attributeNew);
		}
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean checkAttribute (AbstractAttribute existingAttribute, AttributeForm attributeFromPost, Errors errors) {
		if (existingAttribute == null) { 
			AbstractAttribute dAttr = attributeDAO.findAttributeByCode(attributeFromPost.getCode());
			if (dAttr != null) {
			  errors.rejectValue("name", "error.duplicate");
		    }
		} else {
		 if ((existingAttribute instanceof Selectable) && (!((Selectable)existingAttribute).getItems().isEmpty())) {
			Collection<String> diffItems = CollectionUtils.disjunction(((Selectable)existingAttribute).getStringItems(), attributeFromPost.getItems());
			List<String> diffItemsRes = new ArrayList<String>();
			for (String diffItem: diffItems) {
				if (existingAttribute.getStringAttributeValues().contains(diffItem)) {
					diffItemsRes.add(diffItem);
					attributeFromPost.getItems().add(diffItem);
				}
			}
			if (!diffItemsRes.isEmpty()) {
				errors.rejectValue("items", "error.iteminuse", new String[] {diffItemsRes.toString()}, null);				
			}
			
		 }
		 if (!errors.hasFieldErrors("attributeValuesHolder") && 
			 !existingAttribute.getAttributeValuesHolder().equals(attributeFromPost.getAttributeValuesHolder()) &&
			 existingAttribute.hasAttributeValues()) {
			 errors.rejectValue("attributeValuesHolder", "error.attributeValuesHolder");
		 }
		}
		return errors.hasErrors();		
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
	@Transactional(readOnly = false)
	public void softDeleteAttributeById(UUID id) {
		AbstractAttribute attribute = getAttributeById(id);
		if (attribute != null) {
			attribute.setEnabled(false);
			updateAttribute(attribute);
		}
	}

	
	@Override
	public AttributeCategory loadAttributeCategoryById(UUID id, Boolean lock) {
		return attributeCategoryDAO.findById(id, lock);
	}
	
	@Override
	public AbstractAttribute loadAttributeById(UUID id, Boolean lock) {
		return attributeDAO.findById(id, lock);
	}
	
	@Override
	public AbstractAttributeValue loadAttributeValueById(UUID id, Boolean lock) {
		return attributeValueDAO.findById(id, lock);
	}

	@Override
	public List<AttributeCategory> getAllAttributeCategory() {
		List<AttributeCategory> ctg = attributeCategoryDAO.list();
		//list() now can sort if class i assignable from AbstractDomainObjectOrdering
		//Collections.sort(ctg);
		return ctg;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List buildAttributeCategoryHierarchy() {
        List currentHierarchy = new ArrayList();
        List<AttributeCategory> attributeCateries = getAllAttributeCategory();
        List<AbstractAttribute> attributes;
        for (AttributeCategory ctg : attributeCateries) {
        	currentHierarchy.add(ctg);
        	attributes = ctg.getAttributes();
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
	
	@Override
	public AbstractAttribute getAttributeByIdForForm(UUID id) {
		AbstractAttribute at = getAttributeById(id);
		if (at != null && at instanceof Selectable) {
			Hibernate.initialize(((Selectable<?>)at).getItems());
		}
		return at;
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAttributeCategoryById(UUID id) {
		attributeCategoryDAO.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteAttributeById(UUID id) {
		attributeDAO.deleteById(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAttribute(AbstractAttribute attribute) {
		attribute.getAttributeCategory().removeAttribute(attribute);
		Set<Category> cts = attribute.getCategory();
		for (Category ct : cts) {
			if (ct != null) {
				categoryDAO.reattach(ct);
				ct.getAttributes().remove(attribute);
			}
			
		}
		attributeDAO.delete(attribute);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAttributeCategory(AttributeCategory category) {
		attributeCategoryDAO.delete(category);
	}
	
	@Override
	public AttributeCategory cloneAttributeCategoryById(UUID id) throws CloneNotSupportedException {
		AttributeCategory clonedCategory = getAttributeCategoryById(id);
		if (clonedCategory == null) return null;
		return clonedCategory.clone();
	}
	
	@Override
	public AbstractAttribute cloneAttributeById(UUID id) throws CloneNotSupportedException {
		AbstractAttribute clonedAttribute = getAttributeById(id);
		if (clonedAttribute == null) return null;
		return clonedAttribute.clone();
	}

	@Override
	public AbstractAttributeValue getAttributeValueById(UUID id) {
		return attributeValueDAO.get(id);
	}

	@Override
	public boolean isExistsAttributeValue(AbstractAttribute at, Category category) {
		if (category == null) return false;
		LOG.info("Check for existance attribute values of category <"+category.getId().toString()+">");
		return attributeValueDAO.isExistsAttributeValues(at, category);
	}
	
	

}
