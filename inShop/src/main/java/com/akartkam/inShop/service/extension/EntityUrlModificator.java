package com.akartkam.inShop.service.extension;

import com.akartkam.inShop.domain.AbstractWebDomainObject;

public interface EntityUrlModificator {
	void setEntity(AbstractWebDomainObject entity);
	String getPrefixedUrl(String url);
}
