package com.akartkam.inShop.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.formbean.CategoryForm;
import com.akartkam.inShop.service.product.AttributeCategoryService;
import com.akartkam.inShop.service.product.CategoryService;

@Component
public class CategoryValidator implements Validator {
	private static final Log LOG = LogFactory.getLog(CategoryValidator.class);
	@Autowired
	private CategoryService categoryService; 
	
	@Autowired
	private AttributeCategoryService attributeCategoryService;
	
	@Override
	public boolean supports(Class<?> arg0) {
		return Category.class.isAssignableFrom(arg0);
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		CategoryForm category = (CategoryForm) arg0;
		//url check
		if (!errors.hasFieldErrors("urlForForm")) {
			category.buildFullLink(category.getUrlForForm());
			Category exCategory = categoryService.getCategoryByUrl(category.getUrl());
			if (exCategory != null && !exCategory.getId().equals(category.getId())) {
				errors.rejectValue("urlForForm", "error.duplicate");
			}	
		}
		//check for existence of related products for deactivated attributes of this category
		Category exCategory = categoryService.getCategoryById(category.getId());
		if (exCategory != null) {
			List<AbstractAttribute> errAttr = new ArrayList<AbstractAttribute>();
			for (AbstractAttribute at : exCategory.getAttributes()) {
				if (!category.getAttributesForForm().contains(at)) {
					if (attributeCategoryService.isExistsAttributeValue(exCategory)) errAttr.add(at);
				}
			}
			if (!errAttr.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (AbstractAttribute at : errAttr) sb.append(at.getName()).append(", ");
				errors.rejectValue("attributesForForm", "error.hasAttributeValues.attributesForForm", new String[] {sb.toString()}, null);
			}
			//check for change parent category
			if (exCategory.getParent() != null && !exCategory.getParent().equals(category.getParent())) {
				if (exCategory.hasAttributes()) errors.rejectValue("parent", "error.hasAttributes.parent");
			} else {
				if (exCategory.getParent() == null && category.getParent() != null) {
					if (exCategory.hasAttributes()) errors.rejectValue("parent", "error.hasAttributes.parent");
				}
			}
		}
	}

}
