package com.akartkam.inShop.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.service.product.BrandService;


@Component
public class BrandValidator implements Validator {
	private static final Log LOG = LogFactory.getLog(BrandValidator.class);
	@Autowired
	private BrandService brandService; 
	
	
	@Override
	public boolean supports(Class<?> arg0) {
		return Brand.class.isAssignableFrom(arg0);
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		Brand brand = (Brand) arg0;		
		if (!errors.hasFieldErrors("urlForForm")) {
			brand.buildFullLink(brand.getUrlForForm());
			Brand exBrand = brandService.getBrandByUrl(brand.getUrl());
			if (exBrand != null && !exBrand.getId().equals(brand.getId())) {
				errors.rejectValue("urlForForm", "error.duplicate");
			}	
		}
	}

}
