package com.akartkam.inShop.service.customer;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import com.akartkam.inShop.domain.customer.Customer;
import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.formbean.CustomerForm;


public interface CustomerService {
	List<Customer> getAllCustomer();
	Customer getCustomerById(UUID id);
	Errors mergeWithExistingAndUpdateOrCreate (CustomerForm customerForm, BindingResult errors, boolean createAccount) throws IllegalAccessException, InvocationTargetException;
	Customer loadCustomerById(UUID id, Boolean lock);
	void softDeleteCustomerById(UUID id);
	void deleteCustomer(Customer customer);

}
