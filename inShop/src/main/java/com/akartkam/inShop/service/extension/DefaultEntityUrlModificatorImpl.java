package com.akartkam.inShop.service.extension;

import java.util.Properties;

import com.akartkam.inShop.domain.AbstractWebDomainObject;

public class DefaultEntityUrlModificatorImpl implements EntityUrlModificator {

	private AbstractWebDomainObject entity;
    private Properties urlPrefixes;
    
	public Properties getUrlPrefixes() {
		return urlPrefixes;
	}

	public void setUrlPrefixes(Properties urlPrefixes) {
		this.urlPrefixes = urlPrefixes;
	}
	
	@Override
	public void setEntity(AbstractWebDomainObject entity) {
			this.entity = entity;
	}

	@Override
	public String getPrefixedUrl(String url) {
		 String currentPrefix = urlPrefixes.getProperty(entity.getClass().getCanonicalName(), "");
		 StringBuilder prefixedPath = new StringBuilder();
		 if (currentPrefix != null && !"".equals(currentPrefix)) {
			 prefixedPath.append(currentPrefix.startsWith("/")? currentPrefix: "/"+currentPrefix).append(entity.getUrl());
		 } else {
			 prefixedPath.append(entity.getUrl()); 
		 }
			 
		 return prefixedPath.toString();
	}

}
