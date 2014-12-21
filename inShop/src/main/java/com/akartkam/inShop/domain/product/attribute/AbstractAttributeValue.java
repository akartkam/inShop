package com.akartkam.inShop.domain.product.attribute;


import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.akartkam.inShop.domain.AbstractDomainObject;

@MappedSuperclass
public abstract class AbstractAttributeValue extends AbstractDomainObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6088345502855127691L;
	protected AbstractAttribute attribute;
	
	@Transient
	public abstract Serializable getAttributeValue();
	
	@Transient
	public AbstractAttribute getAttribute(){
		return attribute;
	}
	
	public void setAttribute(AbstractAttribute attribute) {
		this.attribute = attribute;
	}
	

}
