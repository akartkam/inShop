package com.akartkam.inShop.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.akartkam.inShop.domain.product.option.ProductOption;

@Component
public class ProductOptionValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ProductOption.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ProductOption po = (ProductOption) target;
		BigDecimal priceAdjustment = po.getProductOptionValues().get(0).getPriceAdjustment();

	}

}
