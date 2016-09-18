package com.akartkam.inShop.service.order;

import com.akartkam.inShop.domain.product.Sku;

public interface InventoryService {
	boolean isAvailable(Sku sku);
	boolean isQuantityAvailable(Sku sku, Integer quantity);
}
