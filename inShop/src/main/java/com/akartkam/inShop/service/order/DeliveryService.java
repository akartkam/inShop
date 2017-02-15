package com.akartkam.inShop.service.order;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.order.Delivery;
import com.akartkam.inShop.domain.order.Store;

public interface DeliveryService {
	Store createStore(Store store);
	Delivery createDelivery(Delivery delivery);
	List<Store> getAllStores();
	List<Delivery> getAllDeliverys();
	Store cloneStoreById(UUID id) throws CloneNotSupportedException;
	Store getStoreById(UUID id);
	Delivery cloneDeliveryById(UUID id) throws CloneNotSupportedException;
	Delivery getDeliveryById(UUID id);
	void mergeWithExistingAndUpdateOrCreate(Store store);
	void mergeWithExistingAndUpdateOrCreate(Delivery delivery);
	
}
