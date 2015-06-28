package com.akartkam.inShop.service.aop;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;


public class AdminRequestMappingAspect {

	public Object setEditTab(ProceedingJoinPoint pjp) throws Throwable {
		BindingResult errors = getBindingResult(pjp.getArgs());
		Class tclass = errors.getTarget().getClass();
		if (errors != null) {
			List<FieldError> lfe = errors.getFieldErrors();
			for (FieldError fe : lfe) {
				System.out.println(fe.getField() + "["
						+ errors.getTarget().getClass().getName() + "]");
				//String mname = "get" + StringUtils.capitalize(fe.getField());
				Method mt = new PropertyDescriptor(fe.getField(), tclass)
						.getReadMethod();
				if (mt != null)
					try {
						for (Annotation an : mt.getAnnotations()) {
							System.out.println(an.toString());
						}
					} catch (SecurityException e) {

					}
			}
		}
		return pjp.proceed();
	}
	
	private BindingResult getBindingResult(Object[] args) {
		for (Object currentArgument : args) {
			if (currentArgument != null){
		       if (currentArgument instanceof BindingResult) {
				return (BindingResult) currentArgument;
			}
		  }     
		}
		return null;
	}
	

}
