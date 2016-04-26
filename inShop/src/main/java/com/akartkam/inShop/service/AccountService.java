package com.akartkam.inShop.service;

import java.util.List;
import java.util.UUID;

import org.springframework.validation.Errors;

import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.formbean.AccountForm;
import com.akartkam.inShop.formbean.AccountableForm;

public interface AccountService {
	void registerAccount(Account account, String password);
	Account getAccountByUsername(String username);
	Account getCurrentAccount();
	List<Account> getAllAccount();
	Account getAccountById(UUID ID);
	void mergeWithExistingAndUpdateOrCreate (AccountForm accountForm, Errors errors);
	void deleteAccount(Account account);
	void softDeleteAccountById(UUID id);
	boolean checkAccountableForm (AccountableForm accountForm, Errors errors);
}
