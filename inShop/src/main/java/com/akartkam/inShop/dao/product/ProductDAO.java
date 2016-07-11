package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.product.Product;

public interface ProductDAO extends GenericDAO<Product, UUID> {
	List<Product> findProductsByName(String name);
}
