package com.akartkam.inShop.service.customer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.akartkam.inShop.dao.customer.CustomerDAO;
import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.customer.Customer;
import com.akartkam.inShop.formbean.CheckoutForm;
import com.akartkam.inShop.formbean.CustomerForm;
import com.akartkam.inShop.service.AccountService;
import com.akartkam.inShop.service.RoleService;
import com.akartkam.inShop.util.NullAwareBeanUtilsBean;

@Service("CustomerService")
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService{
	
	private static final Log LOG = LogFactory.getLog(CustomerServiceImpl.class);
	
	@Autowired
	CustomerDAO customerDAO; 
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	RoleService roleService;
	
	@Override
	@Transactional(readOnly = false)
	public Customer createCustomer(Customer customer){
		return customerDAO.create(customer);
	}

	@Override
	public List<Customer> getAllCustomer() {
		return customerDAO.list();
	}
	
	@Override
	public Customer getCustomerById(UUID id) {
		return customerDAO.get(id);
	}
	
	@Override
	public Customer loadCustomerById(UUID id, Boolean lock) {
		return customerDAO.findById(id, lock);
	}	

	@Override
	@Transactional(readOnly = false)
	public Errors mergeWithExistingAndUpdateOrCreate(CustomerForm customerForm,
			BindingResult errors , boolean createAccount) throws IllegalAccessException , InvocationTargetException {
		if (customerForm == null) return errors;
		BindingResult nerrors = new BeanPropertyBindingResult(errors.getTarget(), errors.getObjectName());
		if (!createAccount) {
			for (ObjectError oe: errors.getGlobalErrors()) nerrors.addError(oe);
			for (FieldError fe: errors.getFieldErrors()) if (!"username".equals(fe.getField())) nerrors.addError(fe);			
		}
		Customer customer = getCustomerById(customerForm.getId());
		if (customer != null) {
			if (!nerrors.hasErrors()) {
				customer.setAddress(customerForm.getAddress());
				customer.setEmail(customerForm.getEmail());
				customer.setEnabled(customerForm.isEnabled());
				customer.setFirstName(customerForm.getFirstName());
				customer.setLastName(customerForm.getLastName());
				customer.setMiddleName(customerForm.getMiddleName());
				customer.setPhone(customerForm.getPhone());
				customer.setBirthdate(customerForm.getBirthdate());
			}
			return nerrors;
		} else {
			BeanUtilsBean bu = new NullAwareBeanUtilsBean();
			Account account = null;
			if (createAccount) {
				if (accountService.checkAccountableForm(customerForm, nerrors)) {
					account = new Account();
					bu.copyProperties(account, customerForm);
					Role role = roleService.getRoleByRoletype(customerForm.getRoletype());
					if (role != null) {
						Set<Role> sr = new HashSet<Role>();
						sr.add(role);
						account.setRoles(sr);
					}
				} else {
					return nerrors;
				}
			}
			if (!nerrors.hasErrors()) { 
				if (createAccount && account != null) accountService.registerAccount(account, customerForm.getPassword());
				customer = new Customer();
				bu.copyProperties(customer, customerForm);
				if (createAccount && account != null) customer.setAccount(account); 
				createCustomer(customer);
			}
			return nerrors;
		}
	}

	@Override
	@Transactional(readOnly = false)	
	public void softDeleteCustomerById(UUID id) {
		Customer customer = getCustomerById(id);
		if (customer != null) {
			customer.setEnabled(false);
		}
	}

	@Override
	public void deleteCustomer(Customer customer) {
		customerDAO.delete(customer);
		
	}

	@Override
	public Customer getCustomer(Account account, CheckoutForm checkoutForm) {
		return customerDAO.findCustomer(account, checkoutForm.getEmail(), checkoutForm.getFirstName(), 
									    checkoutForm.getLastName());
	}





}
