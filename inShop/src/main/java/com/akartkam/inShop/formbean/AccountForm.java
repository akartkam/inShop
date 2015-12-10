package com.akartkam.inShop.formbean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.ScriptAssert;

import com.akartkam.inShop.domain.Account;

@ScriptAssert( lang = "javascript",
			   script = "_this.confirmPassword.equals(_this.password)",
			   message = "account.password.mismatch.message")
public class AccountForm extends Account {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3047051567463301843L;
	private String password, confirmPassword;
	
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
	
	@NotNull
	@NotEmpty
	@Size(min = 6, max = 50)
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
	
}
