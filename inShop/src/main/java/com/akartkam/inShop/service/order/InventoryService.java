package com.akartkam.inShop.service.order;

import java.util.Collection;
import java.util.Map;

import com.akartkam.inShop.domain.product.Sku;

public interface InventoryService {
	boolean isAvailable(Sku sku);
	boolean isQuantityAvailable(Sku sku, int quantity);
	Map<Sku, Integer> retrieveQuantitiesAvailable(Collection<Sku> skus);
	Integer retrieveQuantityAvailable(Sku sku);
}
