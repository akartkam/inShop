package com.akartkam.inShop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="Role" ,
	   uniqueConstraints = {@UniqueConstraint(columnNames = {"role", "name"})})
@NamedQuery(name = "findRoleByName", query = "from Role where name= :name")
public class Role extends AbstractDomainObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6220380899227195583L;
	
	private Roletype role;
	private String name;
	
	@Enumerated
	@Column(name="role")
	@NotNull
	public Roletype getRole() {
		return role;
	}
	public void setRole(Roletype role) {
		this.role = role;
	}
	
	@Column(name="name")
	@NotNull
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
