package com.akartkam.com.presentation.admin;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;

@Aspect
public class AdminRequestMappingAspect {

	@Around("execution(public * *(..)) && @target(org.springframework.stereotype.Controller) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
		/*BindingResult result = null;// = getBindingResult(pjp.getArgs());
		if (result == null) {
			throw new RuntimeException("Binding result is not present in args");
		}*/
		System.out.println("post method called");
		return pjp.proceed();
	}

}
