package com.akartkam.inShop.service.order;

import java.util.Collection;
import java.util.Map;

import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.exception.InventoryUnavailableException;

public interface InventoryService {
	boolean isAvailable(Sku sku);
	boolean isQuantityAvailable(Sku sku, int quantity);
	Map<Sku, Boolean> retrieveIsAvailable(Collection<Sku> skus);
	Map<Sku, Integer> retrieveQuantitiesAvailable(Collection<Sku> skus);
	Integer retrieveQuantityAvailable(Sku sku);
	void decrementInventory(Map<Sku, Integer> skuQuantities) throws InventoryUnavailableException;
	void incrementInventory(Map<Sku, Integer> skuQuantities);
	void incrementInventory(Sku sku, int quant);
	void decrementInventory(Sku sku, int quant) throws InventoryUnavailableException;
}
