package com.akartkam.inShop.common;

import org.hibernate.dialect.HSQLDialect;
import org.hibernate.metamodel.spi.TypeContributions;
import org.hibernate.service.ServiceRegistry;


public class CustomHSQLDialect extends HSQLDialect {
	@Override
    public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.contributeTypes(typeContributions,serviceRegistry);
        typeContributions.contributeType(new UUIDStringCustomType());
    }
}