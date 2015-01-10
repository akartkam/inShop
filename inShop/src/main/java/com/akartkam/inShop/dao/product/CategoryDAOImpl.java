package com.akartkam.inShop.dao.product;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.product.Category;

@Repository
public class CategoryDAOImpl extends AbstractGenericDAO<Category> implements
		CategoryDAO {


	/* Добавил метод list в abstractdao 
	@SuppressWarnings("unchecked")
	@Override
	public List<Category> readAllCategories() {
		Criteria criteria = currentSession().createCriteria(Category.class).
				  add(Restrictions.eq("enabled", true)).addOrder(Order.asc("ordering"));
		criteria.setCacheable(true);
		criteria.setCacheRegion("query.Catalog");
        return (List<Category>) criteria.list();
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> readRootCategories() {
		Criteria criteria = currentSession().createCriteria(Category.class)
				.add(Restrictions.isNull("parent"))
				.add(Restrictions.eq("enabled", true))
				.addOrder(Order.asc("ordering"));
		criteria.setCacheable(true);
		criteria.setCacheRegion("query.Catalog");
        return (List<Category>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> findCategoryByName(String name) {
		Criteria criteria = currentSession().createCriteria(Category.class)
				.add(Restrictions.eq("name", name))
				.add(Restrictions.eq("enabled", true))
				.addOrder(Order.asc("ordering"));
		criteria.setCacheable(true);
		criteria.setCacheRegion("query.Catalog");
        return (List<Category>) criteria.list();
	}

}
