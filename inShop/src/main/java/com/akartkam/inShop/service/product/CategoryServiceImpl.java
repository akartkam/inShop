package com.akartkam.inShop.service.product;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.akartkam.inShop.dao.product.CategoryDAO;
import com.akartkam.inShop.dao.product.ProductDAO;
import com.akartkam.inShop.dao.product.attribute.AttributeDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.formbean.CategoryForm;
import com.akartkam.inShop.util.NullAwareBeanUtilsBean;


@Service("CategoryService")
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
	
	private static final Log LOG = LogFactory.getLog(CategoryServiceImpl.class);
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private AttributeDAO attributeDAO;

	@Override
	@Transactional(readOnly = false)
	public Category createCategory(Category category) {
		return	categoryDAO.create(category);
	}

	@Override
	public List<Category> getRootCategories(Boolean useDisabled) {
		List<Category> rcategories = categoryDAO.readRootCategories(useDisabled);
		return rcategories;
	}

	@Override
	public List<Category> getCategoryByName(String name) {
		return categoryDAO.findCategoryByName(name);
	}

	@Override
	public Category getCategoryByUrl(String url) {
		return categoryDAO.findByUrl(url);
	}	

	@Override
	public List<Category> getAllCategoryHierarchy(Boolean useDisabled) {
		List<Category> allCategoryHierarchy = new ArrayList<Category>();
		for(Category rct: getRootCategories(useDisabled)) {
			rct.buildSubCategoryHierarchy(allCategoryHierarchy, useDisabled);			
		}
		return allCategoryHierarchy;
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
	public void mergeWithExistingAndUpdateOrCreate(final CategoryForm categoryFromPost) {
		if (categoryFromPost == null) return;
		final Category existingCategory = getCategoryById(categoryFromPost.getId());
        // no need to call categoryFromPost.buildFullLink. It calling in validator
		if (existingCategory != null) {
	        // set here explicitly what must/can be overwritten by the html form POST
	        existingCategory.setName(categoryFromPost.getName());
	        existingCategory.setUrl(categoryFromPost.getUrl());
	        existingCategory.setShowQuanPerPackOnProductHeader(categoryFromPost.getShowQuanPerPackOnProductHeader());
	        Category parentCategory = categoryFromPost.getParent();
	        if (parentCategory != null){
	        	parentCategory.addSubCategory(existingCategory);
	        } else { 
	        	if (existingCategory.getParent() != null)  existingCategory.getParent().removeSubCategory(existingCategory);
	        }
	        Iterator<AbstractAttribute> ati = existingCategory.getAttributes().iterator();
	        while(ati.hasNext()){
	        	AbstractAttribute at = ati.next();
	        	if (categoryFromPost.getAttributesForForm().contains(at)) {
	        		categoryFromPost.getAttributesForForm().remove(at);
	        	} else {
	        		ati.remove();
	        	}	        	
	        }
	        List<Category> lc = existingCategory.buildSubCategoryHierarchy(null, true);
	        lc.remove(existingCategory);
	        for (AbstractAttribute attr : categoryFromPost.getAttributesForForm()) {
	        	existingCategory.addAttribute(attr);
		        for (Category c : lc) c.removeAttribute(attr);
		        	
	        }
	        existingCategory.setDescription(categoryFromPost.getDescription());
	        existingCategory.setLongDescription(categoryFromPost.getLongDescription());
	        existingCategory.setOrdering(categoryFromPost.getOrdering());
	        existingCategory.setEnabled(categoryFromPost.isEnabled());
	        existingCategory.setInstruction(categoryFromPost.getInstruction());
	        existingCategory.setH1(categoryFromPost.getH1());
	        existingCategory.setMetaTitle(categoryFromPost.getMetaTitle());
	        existingCategory.setMetaDescription(categoryFromPost.getMetaDescription());
	        existingCategory.setMetaKeywords(categoryFromPost.getMetaKeywords());
	        
		} else {
			//remove attributes of parent categories from this category (because parent attrs nested)
			Category parentCategory = categoryFromPost.getParent();
			//categoryDAO.refresh(parentCategory);
			if (parentCategory != null) {
				List<AbstractAttribute> allParentAttr = parentCategory.getAllAttributes(true);
				Iterator<AbstractAttribute> ati = categoryFromPost.getAttributesForForm().iterator();
				while(ati.hasNext()){
					AbstractAttribute at = ati.next();
					if (allParentAttr.contains(at)) {
						ati.remove();
					}
				}
			}
	        for (AbstractAttribute attr : categoryFromPost.getAttributesForForm()) {
	        	categoryFromPost.addAttribute(attr);
	        }
	        Category category = new Category();
			BeanUtilsBean bu = new NullAwareBeanUtilsBean();
			try {
				bu.copyProperties(category, categoryFromPost);
			} catch (IllegalAccessException | InvocationTargetException e) {
				LOG.error("",e);
			}
			createCategory(category);
		}
    }

	@Override
	@Transactional(readOnly = false)
	public void softDeleteCategoryById(UUID id) {
		Category category = getCategoryById(id);
		if (category != null) {
			category.setEnabled(false);
			updateCategory(category);
		}
		
	}

	@Override
	public Category loadCategoryById(UUID id, Boolean lock) {
		return categoryDAO.findById(id, lock);
	}

	@Override
	public Category cloneCategoryById(UUID id) throws CloneNotSupportedException {
		Category clonedCategory = getCategoryById(id);
		if (clonedCategory == null) return null;
		return clonedCategory.clone();
		
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteCategory(Category category) {
		if (category.hasParentCategory()) {
			Category pcategory = getCategoryById(category.getParent().getId());
			if (pcategory != null) {
				pcategory.getSubCategory().remove(category);
			}
	    }
		categoryDAO.delete(category);
	}



}
