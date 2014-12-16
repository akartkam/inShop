package com.akartkam.inShop.domain.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;

@Entity
@Table(name = "Category")
public class Category extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7426672791950430130L;
	
	private String name;
	private Category parent;
	

	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "name", unique=true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne
	@JoinColumn
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}	

}
