package com.akartkam.inShop.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Version;

public abstract class AbstractDomainObjectHibernateUUID implements DomainObject<UUID> {
	
    private UUID id = GeneratorId.createId();
    private Integer version=0;
    private boolean isActive=true;

	@Override
	@Id
	@Column(name="Id")
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
    @Column(name="Version")
	public Integer getVersion() {
		return version;
	}

	@Override
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Column(name="IsActive")
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}	
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ||
            !(o instanceof DomainObject)) {
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
