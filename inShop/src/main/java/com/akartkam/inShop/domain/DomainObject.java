package com.akartkam.inShop.domain;

import java.io.Serializable;

import org.joda.time.DateTime;


public interface DomainObject <ID extends Serializable> extends Serializable {
    public ID getId();
    public void setId(ID id);

    public Integer getVersion();
    public void setVersion(Integer version);
    
    public boolean isEnabled();
    public void setEnabled(boolean enabled);
    
    public DateTime getCreatedDate();
    public void setCreatedDate(DateTime createdDate);
    
    public DateTime getUpdatedDate();
    public void setUpdatedDate(DateTime updatedDate);
    
    public Account getCreatedBy();
    public void setCreatedBy(Account createdBy);
    
    public Account getUpdatedBy();
    public void setUpdatedBy(Account updatedBy);
}
