package com.akartkam.inShop.listener;


import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.type.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import com.akartkam.inShop.domain.DomainObject;
import com.akartkam.inShop.domain.UserDetailsAdapter;

public class PreInsertAuditEventListener implements PreInsertEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8250021365598369313L;
	private static final Log LOG = LogFactory.getLog(PreInsertAuditEventListener.class);


	@SuppressWarnings("rawtypes")
	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		if (event.getEntity() instanceof DomainObject){
			DomainObject entity = (DomainObject) event.getEntity();
			TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
			DateTime insertDate = new DateTime(DateTimeZone.UTC);
			String[] properties = event.getPersister().getEntityMetamodel().getPropertyNames();
			List<String> propertyList = Arrays.asList(properties);
			Type[] propertyTypes = event.getPersister().getEntityMetamodel().getPropertyTypes();
			for (int i = 0; i<propertyTypes.length; i++) {
				if (propertyTypes[i] instanceof org.hibernate.type.TextType ) {
					String ld = (String) event.getState()[i];
					ld = StringUtils.delete(ld, "<script>");
					ld = StringUtils.delete(ld, "</script>");
					event.getState()[i] = ld;
					try {
						BeanUtils.setProperty(entity, properties[i] , ld);
					} catch (IllegalAccessException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						LOG.error(e.getMessage());
					}
				}
			}			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			event.getState()[propertyList.indexOf("createdDate")] = insertDate;
			event.getState()[propertyList.indexOf("createdBy")] = ((UserDetailsAdapter) authentication.getPrincipal()).getAccount();
			entity.setCreatedDate(insertDate);
			entity.setCreatedBy(((UserDetailsAdapter) authentication.getPrincipal()).getAccount());
		}
		return false;
	}

}
