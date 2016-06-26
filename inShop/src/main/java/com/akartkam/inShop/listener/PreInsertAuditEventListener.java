package com.akartkam.inShop.listener;


import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			event.getState()[propertyList.indexOf("createdDate")] = insertDate;
			event.getState()[propertyList.indexOf("createdBy")] = ((UserDetailsAdapter) authentication.getPrincipal()).getUsername();
			entity.setCreatedDate(insertDate);
			entity.setCreatedBy(((UserDetailsAdapter) authentication.getPrincipal()).getUsername());
		}
		return false;
	}

}
