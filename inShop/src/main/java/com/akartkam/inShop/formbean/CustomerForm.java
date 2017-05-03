package com.akartkam.inShop.formbean;


import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.RoleType;
import com.akartkam.inShop.domain.customer.Customer;

public class CustomerForm extends Customer implements AccountableForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8319307485290038202L;
	private String password = new String() , confirmPassword = new String(), username = new String();
	
	
	public CustomerForm() {	}
	
	public CustomerForm(Customer customer) {
		this.setId(customer.getId());
		this.setLastName(customer.getLastName());
		this.setFirstName(customer.getFirstName());
		this.setMiddleName(customer.getMiddleName());
		this.setAddress(customer.getAddress());
		this.setBirthdate(customer.getBirthdate());
		this.setEmail(customer.getEmail());
		this.setPhone(customer.getPhone());
		Account account = customer.getAccount();
		if (account != null) {
			this.setAccount(customer.getAccount());	
			this.setUsername(account.getUsername());
		}
		this.setCreatedDate(customer.getCreatedDate());
		this.setEnabled(customer.isEnabled());
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public String getConfirmPassword() { 
		return confirmPassword; 
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public RoleType getRoletype() {
		return RoleType.USER;
	}

}
