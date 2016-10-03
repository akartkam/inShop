package com.akartkam.inShop.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.akartkam.inShop.domain.product.InventoryType;
import com.akartkam.inShop.domain.product.Sku;

@Component
public class SkuValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Sku.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Sku sku = (Sku) target;
		if (InventoryType.CHECK_QUANTITY.equals(sku.getInventoryType())) {
			if (sku.getQuantityAvailable() == null) {
				String objName = errors.getObjectName();
				String fieldName;
				if (objName != null && objName.contains("product")) fieldName = "defaultSku.quantityAvailable";
				else fieldName = "quantityAvailable";
				errors.rejectValue(fieldName, "error.null.checkQuantity.quantityAvailable");
			}
		}
	}

}
