package com.akartkam.inShop.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;

public class NullAwareBeanUtilsBean extends BeanUtilsBean {
    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if(value==null)return;
        if(value instanceof Collection)
         if (((Collection<?>) value).isEmpty()) return;  
        if(value instanceof Map) 
         if (((Map<?,?>) value).isEmpty()) return;
        super.copyProperty(dest, name, value);
    }
}
