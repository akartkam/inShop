package com.akartkam.inShop.domain;

import java.io.Serializable;


public interface DomainObject <ID extends Serializable> {
    public ID getId();
    public void setId(ID id);

    public Integer getVersion();
    public void setVersion(Integer version);
    
    public boolean isActive();
    public void setActive(boolean isActive);
}
