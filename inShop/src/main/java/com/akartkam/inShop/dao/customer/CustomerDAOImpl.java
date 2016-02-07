package com.akartkam.inShop.dao.customer;

import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.customer.Customer;

@Repository
public class CustomerDAOImpl extends AbstractGenericDAO<Customer>
implements CustomerDAO {

}
