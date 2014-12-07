package com.akartkam.inShop.domain.goods.attribute;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;

@Entity
@Table(name = "Attribute_Categories")
public class AttributeCategories extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2881965657241346353L;
	private String name;
	
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "name", unique=true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
