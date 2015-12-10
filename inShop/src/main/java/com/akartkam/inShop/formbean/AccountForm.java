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
	private String password;
	
	@NotNull
	@NotEmpty
	@Size(min = 6, max = 50)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
