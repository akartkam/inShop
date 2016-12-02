package com.akartkam.inShop.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.service.extension.ProductDisplayNameModificator;

@Aspect
public class ProductFieldsModifyerExtensionAspect {
	
	@Autowired(required=false)
	private ProductDisplayNameModificator productDisplayNameModificator;

	
	@Around("call(public java.lang.String com.akartkam.inShop.domain.product.Product.getDisplayName())")
	public Object doModifyProductDisplayName(ProceedingJoinPoint pjp) throws Throwable {
		Object ret = pjp.proceed();
		if (productDisplayNameModificator != null) {
			productDisplayNameModificator.setProduct((Product)pjp.getTarget());
			ret = productDisplayNameModificator.getModifyedDisplayName();
		} 
		return ret;
	}

}
