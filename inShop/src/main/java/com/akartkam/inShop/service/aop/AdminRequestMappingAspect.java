package com.akartkam.inShop.service.aop;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.com.presentation.admin.AdminPresentation;
import com.akartkam.com.presentation.admin.EditTab;


public class AdminRequestMappingAspect {

	public Object setEditTab(ProceedingJoinPoint pjp) throws Throwable {
		RedirectAttributes ra = getRedirectAttribute(pjp.getArgs());
		BindingResult errors= getBindingResult(pjp.getArgs());
		if (errors == null || ra == null) {
			ra.addAttribute("tabactive", EditTab.MAIN.getName().toLowerCase());
			return pjp.proceed();
		}
		List<EditTab> et = new ArrayList<EditTab>();
		Class tclass = errors.getTarget().getClass();
		List<FieldError> lfe = errors.getFieldErrors();
		for (FieldError fe : lfe) {
				for (PropertyDescriptor pd: Introspector.getBeanInfo(tclass).getPropertyDescriptors()) {
					System.out.println(pd.getName());
				}
			PropertyDescriptor pd = new PropertyDescriptor(fe.getField(),tclass);
			if (pd != null) {
				Method mt = pd.getReadMethod();
				if (mt != null) {
					AdminPresentation ap = mt.getAnnotation(AdminPresentation.class);
				    et.add(ap.tab());
				}    
			}
		}
		if (!et.isEmpty()) {
			Collections.sort(et, new Comparator <EditTab>() {
				@Override
				public int compare(EditTab o1, EditTab o2) {
					return o2.getDefaultOrder() - o1.getDefaultOrder();
				}});
			ra.addAttribute("tabactive", et.get(0).getName().toLowerCase());
		} else {
			ra.addAttribute("tabactive", EditTab.MAIN.getName().toLowerCase());
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
	
	private RedirectAttributes getRedirectAttribute(Object[] args) {
		for (Object currentArgument : args) {
			if (currentArgument != null){
		       if (currentArgument instanceof RedirectAttributes) {
				return (RedirectAttributes) currentArgument;
			}
		  }     
		}
		return null;
	}
	
}
