package com.akartkam.inShop.dao.product;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.product.Sku;

public interface SkuDAO extends GenericDAO<Sku, UUID> {
	List<Sku> findSkusByName(String name);
	Object[] findSkusByCodeOrNameForPaging(String s, int rowPerPage, int pageNumber);
	Map<UUID, Integer> findMapSkuIdQuantityAvailable(Collection<Sku> skus);
	boolean canDeleteSku (UUID id);
	List<Sku> findActiveSkuList();
}
