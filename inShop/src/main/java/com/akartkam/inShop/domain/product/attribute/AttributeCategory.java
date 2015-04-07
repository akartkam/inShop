package com.akartkam.inShop.domain.product.attribute;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;

@Entity
@Table(name = "Attribute_Category")
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

	@OneToMany(mappedBy = "attributeCategory", cascade = CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public List<AbstractAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<AbstractAttribute> attributes) {
		this.attributes = attributes;
	}	
	
	public void addAttribute (AbstractAttribute attribute) {
		if (attribute == null) throw new IllegalArgumentException("Null attribute!");
		attributes.add(attribute);
		attribute.setAttributeCategory(this);
	}

	public void removeAttribute (AbstractAttribute attribute) {
		if (attribute == null) throw new IllegalArgumentException("Null attribute!");
		attributes.remove(attribute);
		attribute.setAttributeCategory(null);
	}
	
	@Transient
	public boolean hasAttributes() {
		return !attributes.isEmpty(); 
	}	
	
	
}
