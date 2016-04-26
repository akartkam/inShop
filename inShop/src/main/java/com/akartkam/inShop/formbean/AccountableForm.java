package com.akartkam.inShop.formbean;

import java.util.UUID;

import org.hibernate.validator.constraints.ScriptAssert;

import com.akartkam.inShop.domain.DomainObject;

@ScriptAssert( lang = "javascript",
			   script = "_this.confirmPassword.equals(_this.password)",
			   message = "{error.password}")
public interface AccountableForm extends DomainObject<UUID> {
	String getPassword();
	String getConfirmPassword();
	String getUsername();
}
