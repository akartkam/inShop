package com.akartkam.inShop.domain;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@NamedQuery(
		name = "findAccountByUsername",
		query = "from Account where username = :username and enabled = true")
@Entity
@Table(name = "Account")
public class Account extends AbstractDomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4943474187321876921L;
	
	private String username, firstName, lastName, middleName, email, phone, address, city;
	private Set<Role> roles = new HashSet<Role>();
	
	
	@NotEmpty
	@Size(min = 1, max = 20)
	@Column(name = "username", unique=true)
	public String getUsername() { return username; }

	public void setUsername(String username) { this.username = username; }
	
	@NotEmpty
	@Size(min = 1, max = 50)
	@Column(name = "first_name")
	public String getFirstName() { return firstName; }
	
	
	@Column(name = "phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setFirstName(String firstName) { this.firstName = firstName; }
	
	@NotEmpty
	@Size(min = 1, max = 50)
	@Column(name = "last_name")
	public String getLastName() { return lastName; }

	public void setLastName(String lastName) { this.lastName = lastName; }
	
	@Column(name = "middle_name")
	public String getMiddleName() { return middleName; }

	public void setMiddleName(String middleName) { this.middleName = middleName; }

	
	
	@Transient
	public String getFullName() { return firstName + " " + middleName + " " + lastName; }
	

	@NotEmpty
	@Email
	@Column(name = "email")
	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }
	
		
	@ManyToMany
	@JoinTable(
		name = "Account_role",
		joinColumns = { @JoinColumn(name = "account_id", referencedColumnName = "id") },
		inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
	public Set<Role> getRoles() { return roles; }
	
	public void setRoles(Set<Role> roles) { this.roles = roles; }

	@Column(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	

}
