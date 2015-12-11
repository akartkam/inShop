package com.akartkam.inShop.service;



import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.UserDetailsAdapter;
import com.akartkam.inShop.formbean.AccountForm;
import com.akartkam.inShop.dao.AccountDAO;


@Service("accountServiceImpl")
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {
	@Autowired 
	private AccountDAO accountDao;
	
	@Transactional(readOnly = false)
	public void registerAccount(Account account, String password) {
			accountDao.create(account, password);
	}
	
	@Override
	public Account getAccountByUsername(String username) {
		return accountDao.findByUsername(username);
	}
	
	@Override
	public Account getCurrentAccount() {
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    UserDetailsAdapter springSecurityUser = (UserDetailsAdapter) securityContext.getAuthentication().getPrincipal();
	    return springSecurityUser.getAccount();
	}
	
	
	@Override
	public List<Account> getAllAccount() {
		return accountDao.list(); 
	}

	@Override
	public Account getAccountById(UUID ID) {
		return accountDao.get(ID);
	}

	@Override
	public void mergeWithExistingAndUpdateOrCreate(AccountForm accountForm, Errors errors) {
		if (accountForm == null) return;
		if (!checkAccount(accountForm, errors)) return;
		final Account account = getAccountById(accountForm.getId());
		if (account != null) {
			account.setAddress(accountForm.getAddress());
			account.setEmail(accountForm.getAddress());
			account.setEnabled(accountForm.isEnabled());
			account.setFirstName(accountForm.getFirstName());
			account.setLastName(accountForm.getLastName());
			account.setMiddleName(accountForm.getMiddleName());
			account.setPhone(accountForm.getPhone());
			account.setRoles(new HashSet<Role>(accountForm.getRolesList()));
		} else {
			
		}
		
	}
	
	private boolean checkAccount (AccountForm accountForm, Errors errors) {
		for (ObjectError error : errors.getGlobalErrors()) {
			String msg = error.getDefaultMessage();
			if ("account.password.mismatch.message".equals(msg)) {
				if (!errors.hasFieldErrors("password")) {
					errors.rejectValue("password", "error.mismatch");
				}
			}
		}
		
		Account accountExists = getAccountByUsername(accountForm.getUsername());  
		if (!accountExists.getId().equals(accountForm.getId())) {
			errors.rejectValue("username", "error.duplicate",
					new String[] { accountForm.getUsername() }, null);
		}		
		return !errors.hasErrors();
	}

}
