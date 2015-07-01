package com.akartkam.inShop.service.aop;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.com.presentation.admin.AdminPresentation;
import com.akartkam.com.presentation.admin.EditTab;


public class AdminRequestMappingAspect {

	public Object setEditTabMain(ProceedingJoinPoint pjp) throws Throwable {
		Model m = getParam(pjp.getArgs(), Model.class);
		if(m != null && !m.containsAttribute("tabactive")) m.addAttribute("tabactive", EditTab.MAIN.getName().toLowerCase());
		return pjp.proceed();
	}
	
	public Object setEditTab(ProceedingJoinPoint pjp) throws Throwable {
		PropertyDescriptor fpd = null;
		BindingResult errors = getParam(pjp.getArgs(), BindingResult.class);
		RedirectAttributes ra = getParam(pjp.getArgs(), RedirectAttributes.class);
		if (errors == null || ra == null) {
			ra.addFlashAttribute("tabactive", EditTab.MAIN.getName().toLowerCase());
			return pjp.proceed();
		}
		List<AdminPresentation> et = new ArrayList<AdminPresentation>();
		Class<?> tclass = errors.getTarget().getClass();
		List<FieldError> lfe = errors.getFieldErrors();
		for (FieldError fe : lfe) {
			    String fn = prepareFieldName(fe.getField());
				for (PropertyDescriptor pd: Introspector.getBeanInfo(tclass).getPropertyDescriptors()) {
					if (pd.getName().equals(fn)) {
						fpd = pd;
						break;
					}
				}
			
			if (fpd != null) {
				Method mt = fpd.getReadMethod();
				if (mt != null) {
					AdminPresentation ap = mt.getAnnotation(AdminPresentation.class);
					if (ap != null) et.add(ap);
				}    
			}
		}
		if (!et.isEmpty()) {
			Collections.sort(et, new Comparator <AdminPresentation>() {
				@Override
				public int compare(AdminPresentation o1, AdminPresentation o2) {
					int or1, or2;
					if (o1.tabOrder() >= 0) or1 = o1.tabOrder();
					else or1 =  o1.tab().getDefaultOrder();
					if (o2.tabOrder() >= 0) or2 = o2.tabOrder();
					else or2 =  o2.tab().getDefaultOrder();
					return or1 - or2;
				}});
			ra.addFlashAttribute("tabactive", et.get(0).tab().getName().toLowerCase());
		} else {
			ra.addFlashAttribute("tabactive", EditTab.MAIN.getName().toLowerCase());
		}
		return pjp.proceed();
	}
	
	
	@SuppressWarnings("unchecked")
	private <T> T getParam (Object[] args, Class<T> clazz) {
		for (Object currentArgument : args) {
			if (currentArgument != null){
				if (clazz.isAssignableFrom(currentArgument.getClass())) {
					return (T) currentArgument;
				}
  		    }     
		}
		return null;		
	}
	
	private String prepareFieldName(String fn) {
		String res;
		int i = fn.indexOf("[");
		if (i >= 0) {
			res = fn.substring(0, i); 
		} else {
			res = fn;
		}
		return res;
	}
		
}
