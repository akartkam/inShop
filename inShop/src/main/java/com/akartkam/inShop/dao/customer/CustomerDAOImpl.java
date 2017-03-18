package com.akartkam.inShop.dao.customer;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.customer.Customer;

@Repository
public class CustomerDAOImpl extends AbstractGenericDAO<Customer>
implements CustomerDAO {

	@Override
	public Customer findCustomer(Account account, String email, String fname,
			String lname) {
		Criteria criteria = currentSession().createCriteria(Customer.class);
	    if (account != null) criteria.add(Restrictions.eq("account", account));
	    else {
	    	criteria.add(Restrictions.and(Restrictions.eq("email", email != null? email.trim(): null),
	    								  Restrictions.eq("firstName", fname != null? fname.trim(): null),
	    								  Restrictions.eq("lastName", lname != null? lname.trim(): null)));
	    }
	    criteria.addOrder(Order.desc("createdDate"));
	    criteria.setMaxResults(1);
		criteria.setCacheable(true);
		criteria.setCacheRegion("query.Customer");		                    
		return (Customer) criteria.uniqueResult();
	}

}
