package com.akartkam.inShop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NamedQuery(name = "findRoleByName", query = "from Role where name= :name")
@Entity
public class Role extends AbstractDomainObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6220380899227195583L;
	
	private Roletype role;
	private String name;
	
	@Enumerated
	@Column(name="role", unique=true)
	@NotNull
	public Roletype getRole() {
		return role;
	}
	public void setRole(Roletype role) {
		this.role = role;
	}
	
	@Column(name="name", unique=true)
	@NotNull
	@Size(min = 1, max = 50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
