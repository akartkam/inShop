package com.akartkam.inShop.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.UserDetailsAdapter;
import com.akartkam.inShop.dao.AccountDAO;

@Service("accountServiceImpl")
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {
	@Autowired 
	private AccountDAO accountDao;
	
	@Transactional(readOnly = false)
	public boolean registerAccount(Account account, String password, Errors errors) {
		validateUsername(account.getUsername(), errors);
		boolean valid = !errors.hasErrors();
		if (valid) {
			accountDao.create(account, password);
		}
		return valid;
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
	
	private void validateUsername(String username, Errors errors) {
		if (accountDao.findByUsername(username) != null) {
			errors.rejectValue("username", "error.duplicate",
					new String[] { username }, null);
		}
	}
	
	@Override
	public List<Account> getAllAccount() {
		return accountDao.list(); 
	}

}
