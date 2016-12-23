package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Product;

public interface BrandDAO extends GenericDAO<Brand, UUID> {
	List<Brand> readAllBrand(Boolean useDisabled);
	List<Product> findProductsByBrand(Brand brand);
}
