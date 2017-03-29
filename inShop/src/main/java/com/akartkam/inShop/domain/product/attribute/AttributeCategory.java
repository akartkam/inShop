package com.akartkam.inShop.domain.product.attribute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;

@Entity
@Table(name = "Attribute_Category")
@BatchSize(size = 50)
public class AttributeCategory extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2881965657241346353L;
	private String name;
	private List<AbstractAttribute> attributes = new ArrayList<AbstractAttribute>();
	

	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "name", unique=true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "attributeCategory", cascade = CascadeType.ALL)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	@OrderBy("ordering")
	@BatchSize(size = 20)
	public List<AbstractAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<AbstractAttribute> attributes) {
		this.attributes = attributes;
	}	
	
	public void addAttribute (AbstractAttribute attribute) {
		if (attribute == null) throw new IllegalArgumentException("Null attribute!");
		if (getAttributes().contains(attribute)) return;
		if (attribute.getAttributeCategory() != null) removeAttribute(attribute);
		getAttributes().add(attribute);
		attribute.setAttributeCategory(this);
	}

	public void removeAttribute (AbstractAttribute attribute) {
		if (attribute == null) throw new IllegalArgumentException("Null attribute!");
		attribute.getAttributeCategory().getAttributes().remove(attribute);
	}
	
	@Transient
	public boolean hasAttributes() {
		return !attributes.isEmpty(); 
	}	
	
	@Override
	@Transient
	public AttributeCategory clone() throws CloneNotSupportedException {
		AttributeCategory category = (AttributeCategory) super.clone();
		category.setId(UUID.randomUUID());
		category.setName(new String(getName()));
		category.setCreatedBy(null);
		category.setCreatedDate(null);
		category.setUpdatedBy(null);
		category.setUpdatedDate(null);
		category.setAttributes(new ArrayList<AbstractAttribute>());
		return category;
	}	
	
	@Override
	@Transient
	public boolean canRemove() {
		return attributes.isEmpty();
	}
	
	@Transient
	public boolean hasActiveAttributes() {
		for (AbstractAttribute attribute: getAttributes()){
			if (attribute.isEnabled()) return true;
		}
		return false;
	}
	
	
}
