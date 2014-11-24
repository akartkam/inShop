package com.akartkam.inShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import com.akartkam.inShop.domain.Account;

@Service("accountAuditorAwareImpl")
public class AccountAuditorAwareImpl implements AuditorAware<Account> {

    @Autowired 
    private AccountService accountService;

    @Override
    public Account getCurrentAuditor() {
        return accountService.getCurrentAccount();
    }

}
