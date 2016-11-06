package com.akartkam.inShop.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.formbean.CategoryForm;
import com.akartkam.inShop.service.product.CategoryService;

@Component
public class CategoryValidator implements Validator {
	private static final Log LOG = LogFactory.getLog(CategoryValidator.class);
	@Autowired
	private CategoryService categoryService; 
	
	
	@Override
	public boolean supports(Class<?> arg0) {
		return Category.class.isAssignableFrom(arg0);
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		CategoryForm category = (CategoryForm) arg0;
		
		if (!errors.hasFieldErrors("urlForForm")) {
			category.buildFullLink(category.getUrlForForm());
			Category exCategory = categoryService.getCategoryByUrl(category.getUrl());
			if (exCategory != null && !exCategory.getId().equals(category.getId())) {
				errors.rejectValue("urlForForm", "error.duplicate");
			}	
		}
	}

}
