package com.akartkam.inShop.formbean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



import org.hibernate.validator.constraints.ScriptAssert;

import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.Role;


public class AccountForm extends Account implements AccountableForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3047051567463301843L;
	private String password = new String() , confirmPassword = new String();
	private List<Role> rolesList = new ArrayList<Role>();
	
	public AccountForm () { }
	
	public AccountForm (Account account) {
		this.setId(account.getId());
		this.setAddress(account.getAddress());
		this.setCreatedDate(account.getCreatedDate());
		this.setEmail(account.getEmail());
		this.setEnabled(account.isEnabled());
		this.setFirstName(account.getFirstName());
		this.setMiddleName(account.getMiddleName());
		this.setLastName(account.getLastName());
		this.setPhone(account.getPhone());
		this.setRoles(account.getRoles());
		this.setUsername(account.getUsername());
	}
	
	@Override
	public void setRoles(Set<Role> roles) {
		super.setRoles(roles);
		rolesList = new ArrayList<Role>(roles);
	}
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getConfirmPassword() { 
		return confirmPassword; 
	}

	public void setConfirmPassword(String confirmPassword) { 
		this.confirmPassword = confirmPassword; 
	}

	public List<Role> getRolesList() {
		return rolesList;
	}
	
	
	
	
}
