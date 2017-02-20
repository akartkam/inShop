package com.akartkam.inShop.domain.customer;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import com.akartkam.inShop.domain.AbstractDomainObject;
import com.akartkam.inShop.domain.Account;

@Entity
@Table(name = "Customer")
public class Customer extends AbstractDomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3934261574768219676L;
	private String firstName, lastName, middleName, email, phone, address, city;
	private Date birthdate;
	private Account account;
	
	@NotEmpty
	@Size(min = 1, max = 50)
	@Column(name = "first_name")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@NotEmpty
	@Size(min = 1, max = 50)
	@Column(name = "last_name")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(name = "middle_name")
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	@NotEmpty
	@Email
	@Column(name = "email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
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
	
	@DateTimeFormat(pattern="${date.format}")
	@Column(name = "birthdate", columnDefinition = "date")
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	} 
	
	@OneToOne(optional = true)	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}

	@Column(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Transient
	public String getFullName() { return firstName != null ? firstName : "" + " " + 
	                                     middleName != null ? middleName : "" + " " + 
	                                     lastName != null ? lastName : ""  ;
	}	

}
