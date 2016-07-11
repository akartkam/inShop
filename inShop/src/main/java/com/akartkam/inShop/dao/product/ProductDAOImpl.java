package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;

@Repository
public class ProductDAOImpl extends AbstractGenericDAO<Product> implements 
                             ProductDAO {
	
	@Autowired
	private SkuDAO skuDAO;
	
	@Override
	public List<Product> findProductsByName(String name) {
		List<Sku> skus = skuDAO.findSkusByName(name);
		List<Product> products = new ArrayList<Product>();
		for (Sku sku : skus) {
			if (sku.getDefaultProduct() != null) {
			   if (!products.contains(sku.getDefaultProduct())) products.add(sku.getDefaultProduct());
			} else {
			   if (!products.contains(sku.getProduct())) products.add(sku.getProduct());
			}
		}
		return products;
		
	}

}
