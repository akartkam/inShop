package com.akartkam.inShop.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;


public class AdminRequestMappingAspect {

	//@Around("execution(public * *(..)) && @target(org.springframework.stereotype.Controller)")
	public Object setEditTab(ProceedingJoinPoint pjp) throws Throwable {
		/*BindingResult result = null;// = getBindingResult(pjp.getArgs());
		if (result == null) {
			throw new RuntimeException("Binding result is not present in args");
		}*/
		System.out.println("request method called "+pjp.METHOD_CALL );
		return pjp.proceed();
	}

}
