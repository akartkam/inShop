package com.akartkam.inShop.service.order;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.order.Store;

public interface DeliveryService {
	Store createStore(Store store);
	List<Store> getAllStores();
	Store cloneStoreById(UUID id) throws CloneNotSupportedException;
	Store getStoreById(UUID id);
	void mergeWithExistingAndUpdateOrCreate(Store store);
}
