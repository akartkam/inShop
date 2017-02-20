package com.akartkam.inShop.formbean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.validator.HtmlSafe;

public class CheckoutForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2251596240922177675L;
	private String firstName, lastName, email, phone, address, city;
	private String customerComment;

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
	
	@NotEmpty
	@Email
	@Column(name = "email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@NotEmpty
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
	
	@Column(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@HtmlSafe
    @Lob
    @Type(type = "org.hibernate.type.TextType")
	@Column(name = "customer_comment", length = Integer.MAX_VALUE - 1)
	public String getCustomerComment() {
		return customerComment;
	}
	public void setCustomerComment(String customerComment) {
		this.customerComment = customerComment;
	}
	
}
