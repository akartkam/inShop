package com.akartkam.inShop.dao.customer;

import java.util.UUID;

import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.customer.Customer;

public interface CustomerDAO extends GenericDAO<Customer, UUID> {
	Customer findCustomer(Account account, String email, String fname, String lname);
}
