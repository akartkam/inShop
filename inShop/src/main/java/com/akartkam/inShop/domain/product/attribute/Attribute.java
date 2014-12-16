package com.akartkam.inShop.domain.product.attribute;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Attribute extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1978623223082601133L;
	private String name;
	private AttributeCategory attributeCategory;
	
	@NotNull
	@Column(name = "name", unique=true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "attribueType")
	public abstract AttribueType getAttribueType();
	
	public AttributeCategory getAttributeCategory() {
		return attributeCategory;
	}
	public void setAttributeCategory(AttributeCategory attributeCategory) {
		this.attributeCategory = attributeCategory;
	}	

}
