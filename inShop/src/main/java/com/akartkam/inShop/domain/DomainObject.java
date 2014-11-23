package com.akartkam.inShop.domain;

import java.io.Serializable;


public interface DomainObject <ID extends Serializable> extends Serializable {
    public ID getId();
    public void setId(ID id);

    public Integer getVersion();
    public void setVersion(Integer version);
    
    public boolean isEnabled();
    public void setEnabled(boolean enabled);
}
