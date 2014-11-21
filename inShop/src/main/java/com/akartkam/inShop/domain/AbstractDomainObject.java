package com.akartkam.inShop.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractDomainObject implements DomainObject<UUID> {
	
    private UUID id = GeneratorId.createId();
    private Integer version=0;
    private boolean enabled=true;
    private DateTime createdDate;  
    //@CreatedBy  
    //private  createdBy;  
      
    private DateTime updatedDate;  
    //@LastModifiedBy  
    //private  updatedBy;      

	@Override
	@Id
	@Column(name="id")
	@org.hibernate.annotations.Type(type="pg-uuid")
	public UUID getId() {
		return id;
	}

	@Override
	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	@Version	
    @Column(name="version")
	public Integer getVersion() {
		return version;
	}

	@Override
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Column(name="enabled")
	public boolean isActive() {
		return enabled;
	}

	public void setActive(boolean isActive) {
		this.enabled = isActive;
	}	
	
    @CreatedDate 
    @Column(name="createdDate")
	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	@LastModifiedDate
	 @Column(name="updatedDate")
	public DateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(DateTime updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ||
            !(o instanceof DomainObject) 
            // looks into the target class of a proxy if necessary
        	|| ! (Hibernate.getClass(o).equals(Hibernate.getClass(this)))){
            return false;
        }
        
        @SuppressWarnings("unchecked")
        DomainObject<UUID> other = (DomainObject<UUID>)o;
        // if the id is missing, return false
        if (id == null) return false;
        // equivalence by id
        return id.equals(other.getId());
    }

	@Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        } else {
            return super.hashCode();
        }
    }

	@Override
    public String toString() {
        return this.getClass().getName()
            + "[id=" + id + "]";
    }	

}
