package com.akartkam.inShop.dao;

import java.util.UUID;

import com.akartkam.inShop.domain.Account;

public interface AccountDAO extends GenericDao<Account, UUID> {
	void create(Account account, String password);
	Account findByUsername(String username);
}
