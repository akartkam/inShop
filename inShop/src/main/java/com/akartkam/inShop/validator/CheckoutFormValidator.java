package com.akartkam.inShop.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.akartkam.inShop.domain.order.Delivery;
import com.akartkam.inShop.domain.order.DeliveryType;
import com.akartkam.inShop.domain.order.Store;
import com.akartkam.inShop.formbean.CheckoutForm;

@Component
public class CheckoutFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CheckoutForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CheckoutForm checkoutForm = (CheckoutForm) target;
		
		Delivery delivery = checkoutForm.getDelivery(); 
		Store store = checkoutForm.getStore(); 
		if (delivery != null && delivery.getDeliveryType().equals(DeliveryType.SELF_DELIVERY) && store == null ) {
			errors.rejectValue("store", "error.checkout.empty.store");
		}
		
	}


}
