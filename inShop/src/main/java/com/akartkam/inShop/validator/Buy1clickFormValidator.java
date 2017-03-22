package com.akartkam.inShop.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.akartkam.inShop.formbean.Buy1clickForm;

@Component
public class Buy1clickFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Buy1clickForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "name", "error.empty.buy1clickForm.name");
		ValidationUtils.rejectIfEmpty(errors, "phone", "error.empty.buy1clickForm.phone");
	}

}
