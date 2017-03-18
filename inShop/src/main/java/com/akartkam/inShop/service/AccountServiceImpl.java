package com.akartkam.inShop.service;



import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.UserDetailsAdapter;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.formbean.AccountForm;
import com.akartkam.inShop.formbean.AccountableForm;
import com.akartkam.inShop.util.NullAwareBeanUtilsBean;
import com.akartkam.inShop.dao.AccountDAO;


@Service("accountServiceImpl")
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {
	private static final Log LOG = LogFactory.getLog(AccountServiceImpl.class);
	
	@Autowired 
	private AccountDAO accountDao;
	
	@Autowired
	private MessageSource messageSource;
	
	@Value("#{appProperties['inShop.passMinSize']}")
	private String passMinSize;	
	
	@Value("#{appProperties['inShop.passMaxSize']}")
	private String passMaxSize;
	
	@Override
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
	@Transactional(readOnly = false)
	public void deleteAccount(Account account) {
		accountDao.delete(account);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void softDeleteAccountById(UUID id) {
		Account account = getAccountById(id);
		if (account != null) {
			account.setEnabled(false);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(AccountForm accountForm, Errors errors) {
		if (accountForm == null) return;
		if (!checkAccountableForm(accountForm, errors)) return;
		Account account = getAccountById(accountForm.getId());
		if (account != null) {
			account.setAddress(accountForm.getAddress());
			account.setEmail(accountForm.getEmail());
			account.setEnabled(accountForm.isEnabled());
			account.setFirstName(accountForm.getFirstName());
			account.setLastName(accountForm.getLastName());
			account.setMiddleName(accountForm.getMiddleName());
			account.setPhone(accountForm.getPhone());
			Iterator<Role> ri = account.getRoles().iterator();
	        while(ri.hasNext()){
	        	Role r = ri.next();
	        	if (accountForm.getRolesList().contains(r)) {
	        		accountForm.getRolesList().remove(r);
	        	} else {
	        		ri.remove();
	        	}
	        	
	        }
	        for (Role rlEx : accountForm.getRolesList()) {
	        	account.getRoles().add(rlEx);
	        }			
			String newPassword = accountForm.getPassword();
			if (newPassword != null && !"".equals(newPassword)) {
				accountDao.updatePassword(account, newPassword);
			}
		} else {
			account = new Account();
			BeanUtilsBean bu = new NullAwareBeanUtilsBean();
			try {
				bu.copyProperties(account, accountForm);
			} catch (IllegalAccessException | InvocationTargetException e) {
				LOG.error("",e);
			}
			account.setRoles(new HashSet<Role>(accountForm.getRolesList()));
			registerAccount(account, accountForm.getPassword());
		}
	}
	
	@Override
	public boolean checkAccountableForm (AccountableForm accountForm, Errors errors) {
		for (ObjectError error : errors.getGlobalErrors()) {
			String errMsg = error.getDefaultMessage();
			String msg = messageSource.getMessage("error.password", null, Locale.getDefault());
			if (errMsg.equals(msg)) {
				if (!errors.hasFieldErrors("password")) {
					errors.rejectValue("password", "error.mismatch");
				}
			}
		}
		String pass = accountForm.getPassword();
		
		if (accountForm.isNew()) {
			if (pass == null || "".equals(pass)) {
			errors.rejectValue("password", "error.empty");
			}
		}

		if (pass != null && !"".equals(pass)) {
			if (pass.length()<Integer.parseInt(passMinSize) || pass.length()> Integer.parseInt(passMaxSize)) 
				  errors.rejectValue("password", "error.size", new String[]{passMinSize, passMaxSize}, null); 
		}
		String username = accountForm.getUsername();
		if (username != null  && !"".equals(username)) {
			Account accountExists = getAccountByUsername(username);
			if (accountExists != null && !accountExists.getId().equals(accountForm.getId())) {
				errors.rejectValue("username", "error.duplicate",
						new String[] { accountForm.getUsername() }, null);
			}
		}
		return !errors.hasErrors();
	}

}
