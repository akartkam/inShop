package com.akartkam.inShop.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;


@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractDomainObject implements DomainObject<UUID>, Cloneable {
	
	private UUID id = GeneratorId.createId();
    private Integer version=0;
    private boolean enabled=true;
    private DateTime createdDate;   
    private String createdBy;  
	private DateTime updatedDate;  
    private String  updatedBy;      
    public String tag; //not persistence, for common use

	@Id
	@Column(name="id")
	@org.hibernate.annotations.Type(type="pg-uuid")
	public UUID getId() {
		return this.id;
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
	
	@DateTimeFormat(pattern="${date.format}")
    @Column(name="createdDate", columnDefinition = "timestamp with time zone")
	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}


	@Column(name="updatedDate", columnDefinition = "timestamp with time zone")
	public DateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(DateTime updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	  
	@Column(name = "createBy")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "updatedBy")
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Column(name="enabled")
    public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}	
    
	
	@Override
    @SuppressWarnings("rawtypes")
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ||
            !(o instanceof DomainObject) 
            // looks into the target class of a proxy if necessary
        	|| !(Hibernate.getClass(o).equals(Hibernate.getClass(this))))
        {
            return false;
        }
        DomainObject other = (DomainObject)o;
        // if the id is missing, return false
        if (id == null) return false;
        // equivalence by id
        return id.equals(other.getId());
    }
	
	@Override
	@Transient
	public boolean isNew() {
		return (getCreatedDate() == null);
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
	
	@Override
	@Transient
	public boolean canRemove(){
		return true;
	}
	
	
}
