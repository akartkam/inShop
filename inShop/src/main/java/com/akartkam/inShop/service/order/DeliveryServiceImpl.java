package com.akartkam.inShop.service.order;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.order.StoreDAO;
import com.akartkam.inShop.domain.order.Store;

@Service("DeliveryService")
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {

	@Autowired
	private StoreDAO storeDAO;
	
	@Override
	public List<Store> getAllStores() {
		return storeDAO.list();
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

}
