package com.akartkam.inShop.listener;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.akartkam.inShop.domain.DomainObject;
import com.akartkam.inShop.domain.UserDetailsAdapter;

public class PreUpdateAuditEventListener implements PreUpdateEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7564747933532379951L;
	private static final Log LOG = LogFactory.getLog(PreUpdateAuditEventListener.class);

	@Override
	@SuppressWarnings("rawtypes")
	public boolean onPreUpdate(PreUpdateEvent event) {
		if (event.getEntity() instanceof DomainObject) {
			DomainObject entity = (DomainObject) event.getEntity();
			TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
			DateTime insertDate = new DateTime(DateTimeZone.UTC);
			String[] properties = event.getPersister().getEntityMetamodel().getPropertyNames();
			List<String> propertyList = Arrays.asList(properties);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			String princName = "";
			if (authentication.getPrincipal() != null && !authorities.contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))){
				princName = ((UserDetailsAdapter) authentication.getPrincipal()).getUsername();
			} else {
				princName = "UserByDefault";	
			}
			event.getState()[propertyList.indexOf("updatedBy")] = princName;
			entity.setCreatedBy(princName);				
			event.getState()[propertyList.indexOf("updatedDate")] = insertDate;
			entity.setUpdatedDate(insertDate);
		}
		return false;
	}

}
