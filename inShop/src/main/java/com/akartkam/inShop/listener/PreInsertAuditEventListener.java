package com.akartkam.inShop.listener;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.DomainObject;
import com.akartkam.inShop.domain.UserDetailsAdapter;

public class PreInsertAuditEventListener implements PreInsertEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8250021365598369313L;

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
			//event.getState()[propertyList.indexOf("updatedDate")] = insertDate;
			event.getState()[propertyList.indexOf("createdBy")] = ((UserDetailsAdapter) authentication.getPrincipal()).getAccount();
			//event.getState()[propertyList.indexOf("updatedBy")] = ((UserDetailsAdapter) authentication.getPrincipal()).getAccount();
			entity.setCreatedDate(insertDate);
			//entity.setUpdatedDate(insertDate);
			entity.setCreatedBy(((UserDetailsAdapter) authentication.getPrincipal()).getAccount());
			//entity.setUpdatedBy(((UserDetailsAdapter) authentication.getPrincipal()).getAccount());
		}
		return false;
	}

}
