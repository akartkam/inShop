package com.akartkam.inShop.dao.product;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;

@Repository
public class BrandDAOImpl extends AbstractGenericDAO<Brand> implements BrandDAO {
	@SuppressWarnings("unchecked")
	@Override
	public List<Brand> readAllBrand(Boolean useDisabled) {
		Criteria criteria = currentSession().createCriteria(Brand.class);
		if (!useDisabled) criteria.add(Restrictions.eq("enabled", true));
		criteria.setCacheable(true);
		criteria.setCacheRegion("query.Catalog");
        return (List<Brand>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findProductsByBrand(Brand brand) {
		Query q = currentSession().getNamedQuery("findProductByBrand");
		q.setParameter("brand", brand);
		return  (List<Product>)q.list();
	}
	
	
}
