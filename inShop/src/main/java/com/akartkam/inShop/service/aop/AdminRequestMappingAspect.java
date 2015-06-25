package com.akartkam.inShop.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@Aspect
public class AdminRequestMappingAspect {

	@Around("execution(public * *(..)) && @target(org.springframework.stereotype.Controller)")
	public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
		/*BindingResult result = null;// = getBindingResult(pjp.getArgs());
		if (result == null) {
			throw new RuntimeException("Binding result is not present in args");
		}*/
		System.out.println("request method called "+pjp.getClass().getName());
		return pjp.proceed();
	}

}
