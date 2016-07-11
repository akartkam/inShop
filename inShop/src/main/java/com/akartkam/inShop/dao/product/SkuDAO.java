package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.product.Sku;

public interface SkuDAO extends GenericDAO<Sku, UUID> {
	List<Sku> findSkusByName(String name);
}
