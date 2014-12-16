package com.akartkam.inShop.domain.product.attribute;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.product.Category;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Attribute<T extends AttributeValue> extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1978623223082601133L;
	private String name;
	private AttributeCategory attributeCategory;
	private Category category;
	

	@NotNull
	@Column(name = "name", unique=true, nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "attribueType", nullable=false)
	public abstract AttribueType getAttribueType();
	
	public abstract List<T> getAttributeValue();
	
	@ManyToOne
	@JoinColumn
	public AttributeCategory getAttributeCategory() {
		return attributeCategory;
	}
	public void setAttributeCategory(AttributeCategory attributeCategory) {
		this.attributeCategory = attributeCategory;
	}	

	@NotNull
	@ManyToOne
	@JoinColumn(nullable=false)
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}	

}
