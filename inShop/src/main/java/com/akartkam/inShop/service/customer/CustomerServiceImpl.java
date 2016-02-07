package com.akartkam.inShop.service.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.customer.CustomerDAO;
import com.akartkam.inShop.domain.customer.Customer;

@Service("CustomerService")
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	CustomerDAO customerDAO; 

	@Override
	public List<Customer> getAllCustomer() {
		return customerDAO.list();
	}

}
