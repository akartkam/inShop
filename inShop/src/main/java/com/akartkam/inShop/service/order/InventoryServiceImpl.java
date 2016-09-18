package com.akartkam.inShop.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.product.SkuDAO;
import com.akartkam.inShop.domain.product.InventoryType;
import com.akartkam.inShop.domain.product.Sku;

@Service("InventoryService")
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private SkuDAO skuDAO; 
	
	@Override
	public boolean isAvailable(Sku sku) {
		return isQuantityAvailable(sku, 1);
	}

	@Override
	public boolean isQuantityAvailable(Sku sku, Integer quantity) {
		if (sku == null) return false;
		//try to refresh to know exact value of balance
		skuDAO.refresh(sku);
		if (InventoryType.UNAVAILABLE.equals(sku.getInventoryType())) return false;
		InventoryType it;
		if (sku.hasDefaultSku() ) {
			it = sku.getInventoryType();
			if (sku.getDefaultProduct().isCanSellWithoutOptions()) {
			    if (InventoryType.CHECK_QUANTITY.equals(it)) {
			    	return sku.getQuantityAvailable() >= quantity;
			    } else if (InventoryType.ALWAYS_AVAILABLE.equals(it)) {
			    	return true;
			    }
			} else {
				return false;
			}
		} else {
			it = sku.lookupDefaultSku().getInventoryType();
		    if (InventoryType.CHECK_QUANTITY.equals(it)) {
		    	return sku.getQuantityAvailable() >= quantity;
		    } else if (InventoryType.ALWAYS_AVAILABLE.equals(it)) {
		    	return true;
		    }			
		}
		return true;		
	}

}
