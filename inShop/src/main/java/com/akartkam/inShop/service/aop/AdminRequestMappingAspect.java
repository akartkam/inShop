package com.akartkam.inShop.service.aop;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.controller.admin.product.AdminBrandController;
import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;
import com.akartkam.inShop.util.SecurityTools;
import com.akartkam.inShop.validator.HtmlSafe;


public class AdminRequestMappingAspect {
	private static final Log LOG = LogFactory.getLog(AdminRequestMappingAspect.class);
	/*Нижеследующий код для внедрения CustomEditor в IninBinder для XSS в полях description*/
	public Object injectCustomEditorAntiXSS(ProceedingJoinPoint pjp) throws Throwable {
		Object ret = pjp.proceed();
		DataBinder db = getParam(pjp.getArgs(), WebDataBinder.class);
		Object target = db.getTarget();
		if (target != null) {
			List<HtmlSafe> et = new ArrayList<HtmlSafe>();
			String[] af = db.getAllowedFields();
			String fs = findStrIntoStrArray(af, "description");
			if (fs == null)  return ret;
			PropertyEditor exPe = db.findCustomEditor(String.class, fs);
			if (exPe != null && exPe.getClass().equals(PropertyEditorSupportAntiXSSProxy.class)) return ret;
			PropertyEditor pe = (exPe != null) ? new PropertyEditorSupportAntiXSSProxy(exPe): new PropertyEditorSupportAntiXSSProxy();
			db.registerCustomEditor(String.class, fs, pe);	
		}
		return ret;
	}
	
	/*Нижеследующий код для аспекта определяющего правильную страницу tab в окне редактирования*/
	public Object setEditTabMain(ProceedingJoinPoint pjp) throws Throwable {
		Model m = getParam(pjp.getArgs(), Model.class);
		if(m != null && !m.containsAttribute("tabactive")) m.addAttribute("tabactive", EditTab.MAIN.getName().toLowerCase());
		return pjp.proceed();
	}
	
	public Object setEditTab(ProceedingJoinPoint pjp) throws Throwable {
		Object ret = pjp.proceed();
		PropertyDescriptor fpd = null;
		BindingResult errors = getParam(pjp.getArgs(), BindingResult.class);
		RedirectAttributes ra = getParam(pjp.getArgs(), RedirectAttributes.class);
		if (errors == null || ra == null) {
			ra.addFlashAttribute("tabactive", EditTab.MAIN.getName().toLowerCase());
			return ret;
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
		return ret;
	}
	
	private String findStrIntoStrArray (String[] ar, String fs){
		if (ar != null)
			for (String cs: ar) {
				if (cs.contains(fs)) return cs;
			}
		return null;
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
	
	static private class PropertyEditorSupportAntiXSSProxy extends PropertyEditorSupport {
		
		private PropertyEditor propertyEditor = null;
		public PropertyEditorSupportAntiXSSProxy() {}
		public PropertyEditorSupportAntiXSSProxy (PropertyEditor propertyEditor) {
			this.propertyEditor = propertyEditor;
		}

	    @Override
	    public void setAsText(String text) {
	    	if (propertyEditor != null) {
	    		propertyEditor.setAsText(text);
	    	} else 	if ("".equals(text) || "''".equals(text))  setValue(null);
         	setValue(SecurityTools.stripXSS(text));	
	    }	    
	}
		
}
