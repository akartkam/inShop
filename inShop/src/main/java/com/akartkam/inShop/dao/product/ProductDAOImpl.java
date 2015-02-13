package com.akartkam.inShop.dao.product;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;

@Repository
public class ProductDAOImpl extends AbstractGenericDAO<Product> implements 
                             ProductDAO {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findProductByName(String name){ 
		Criteria criteria = currentSession().createCriteria(Product.class)
				.add(Restrictions.eq("name", name))
				.add(Restrictions.eq("enabled", true))
				.addOrder(Order.asc("ordering"));
		criteria.setCacheable(true);
		criteria.setCacheRegion("query.Catalog");
        return (List<Product>) criteria.list();		
	}

}
