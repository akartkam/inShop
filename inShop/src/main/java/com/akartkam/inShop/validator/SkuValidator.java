package com.akartkam.inShop.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.akartkam.inShop.controller.admin.order.AdminOrderController;
import com.akartkam.inShop.domain.product.InventoryType;
import com.akartkam.inShop.domain.product.Sku;

@Component
public class SkuValidator implements Validator {
	private static final Log LOG = LogFactory.getLog(AdminOrderController.class);
	@Override
	public boolean supports(Class<?> clazz) {
		return Sku.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Sku sku = (Sku) target;
		Class<?> t = errors.getClass();
		LOG.info(t.getName());
		if (InventoryType.CHECK_QUANTITY.equals(sku.getInventoryType())) {
			if (sku.getQuantityAvailable() == null) {
				String objName = errors.getObjectName();
				String fieldName = "quantityAvailable";
				if (objName != null && objName.contains("product")) fieldName = "defaultSku.quantityAvailable";
				errors.rejectValue(fieldName, "error.null.checkQuantity.quantityAvailable");
			}
		}
	}

}
