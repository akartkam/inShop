package com.akartkam.inShop.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.service.extension.ProductDisplayNameModificator;

@Aspect
public class ProductFieldsModifyerExtensionAspect {
	
	private ProductDisplayNameModificator productDisplayNameModificator;
	
	
	public ProductDisplayNameModificator getProductDisplayNameModificator() {
		return productDisplayNameModificator;
	}


	public void setProductDisplayNameModificator(
			ProductDisplayNameModificator productDisplayNameModificator) {
		this.productDisplayNameModificator = productDisplayNameModificator;
	}


	@Around("execution(public java.lang.String com.akartkam.inShop.domain.product.Product.getDisplayName())")
	public Object doModifyProductDisplayName(ProceedingJoinPoint pjp) throws Throwable {
		Object ret = pjp.proceed();
		if (productDisplayNameModificator != null) {
			productDisplayNameModificator.setSku((Sku)pjp.getTarget());
			ret = productDisplayNameModificator.getModifyedDisplayName((String)ret);
		} 
		return ret;
	}

}
