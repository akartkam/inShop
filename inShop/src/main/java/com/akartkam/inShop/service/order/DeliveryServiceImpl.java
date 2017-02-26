package com.akartkam.inShop.service.order;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.order.DeliveryDAO;
import com.akartkam.inShop.dao.order.StoreDAO;
import com.akartkam.inShop.domain.order.Delivery;
import com.akartkam.inShop.domain.order.Store;
import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.option.ProductOption;

@Service("DeliveryService")
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {

	@Autowired
	private StoreDAO storeDAO;

	@Autowired
	private DeliveryDAO deliveryDAO;
	
	
	@Override
	public List<Store> getAllStores() {
		return storeDAO.list();
	}

	@Override
	@Transactional(readOnly = false)
	public Store createStore(Store store) {
		return storeDAO.create(store);
	}
	
	@Override
	public Store cloneStoreById(UUID id) throws CloneNotSupportedException {
		Store clonedStore = getStoreById(id);
		if (clonedStore == null) return null;
		return clonedStore.clone();
	}

	@Override
	public Store getStoreById(UUID id) {
		return storeDAO.get(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(Store store) {
		if (store == null) return;
		final Store existingStore = getStoreById(store.getId());
		if (existingStore != null) {
			existingStore.setName(store.getName());
			existingStore.setAddress(store.getAddress());
			existingStore.setLongDescription(store.getLongDescription());
			existingStore.setEnabled(store.isEnabled());
			existingStore.setImageUrl(store.getImageUrl());
			existingStore.setMapScript(store.getMapScript());
			existingStore.setWorkSchedule(store.getWorkSchedule());
			existingStore.setPhone(store.getPhone());
			existingStore.setOrdering(store.getOrdering());
		} else {
			createStore(store);
		}		
	}

	@Override
	@Transactional(readOnly = false)
	public Delivery createDelivery(Delivery delivery) {
		return deliveryDAO.create(delivery);
	}

	@Override
	public List<Delivery> getAllDeliverys() {
		return deliveryDAO.list();
	}

	@Override
	public Delivery cloneDeliveryById(UUID id)
			throws CloneNotSupportedException {
		Delivery clonedDelivery = getDeliveryById(id);
		if (clonedDelivery == null) return null;
		return clonedDelivery.clone();
	}

	@Override
	public Delivery getDeliveryById(UUID id) {
		return deliveryDAO.get(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(Delivery delivery) {
		if (delivery == null) return;
		final Delivery existingDelivery = getDeliveryById(delivery.getId());
		if (existingDelivery != null) {
			existingDelivery.setName(delivery.getName());
			existingDelivery.setDeliveryType(delivery.getDeliveryType());
			existingDelivery.setIsPublic(delivery.getIsPublic());
			existingDelivery.setLongDescription(delivery.getLongDescription());
			existingDelivery.setOrdering(delivery.getOrdering());
			Iterator<Store> ist = existingDelivery.getStores().iterator();
	        while(ist.hasNext()){
	        	Store st = ist.next();
	        	if (delivery.getStores().contains(st)) {
	        		delivery.removeStore(st);
	        	} else {
	        		ist.remove();
	        	}	        	
	        }
	        for (Store stEx : delivery.getStores()) {
	        	existingDelivery.addStore(stEx);
	        }			
		} else {
			createDelivery(delivery);
		}		
	}

	@Override
	public Store loadStoreById(UUID id, boolean lock) {
		return storeDAO.findById(id, lock);
	}

	@Override
	public Delivery loadDeliveryById(UUID id, boolean lock) {
		return deliveryDAO.findById(id, lock);
	}

}
