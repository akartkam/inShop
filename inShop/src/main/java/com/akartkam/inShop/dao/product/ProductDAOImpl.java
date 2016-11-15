package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.ProductStatus;
import com.akartkam.inShop.domain.product.Sku;

@Repository
public class ProductDAOImpl extends AbstractGenericDAO<Product> implements 
                             ProductDAO {
	
	private String s = "select p.* from product p, sku s, lnk_product_status lp where s.id=p.default_sku_id and lp.sku_id=s.id;";
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
	


	@Override
	public List<Product> findProductsByProductStatus(ProductStatus  productStatus) {
		Query q = currentSession().getNamedQuery("findProductByProductStatus");
		q.setParameter("productStatus", productStatus.toString());
		return q.list();
	}

}
