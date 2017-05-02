package com.akartkam.inShop.dao;

import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ReportsDAOImpl implements ReportsDAO {
	
    @Autowired
	private SessionFactory sessionFactory;	
    
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }   

	@Override
	public Object[] findOrdersCheck(UUID order_id) {
		Query query = currentSession().getNamedQuery("orderCheckQuery").setString(0, order_id.toString());
		Object[] ret = (Object[]) query.uniqueResult();
		return ret;
	}
	
	@Override
	public List<Object[]> findOrderItemsCheck(UUID order_id) {
		Query query = currentSession().getNamedQuery("orderItemCheckQuery").setString(0, order_id.toString());
		List<Object[]> ret = (List<Object[]>)query.list();
		return ret;
	}
}
