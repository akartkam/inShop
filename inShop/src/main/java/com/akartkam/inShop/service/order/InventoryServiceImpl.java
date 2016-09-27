package com.akartkam.inShop.service.order;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.product.SkuDAO;
import com.akartkam.inShop.domain.product.InventoryType;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.exception.ProductNotFoundException;
import com.akartkam.inShop.util.CommonUtil;

@Service("InventoryService")
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private SkuDAO skuDAO; 
	
	@Override
	@Transactional(readOnly = true)
	public Map<Sku, Integer> retrieveQuantitiesAvailable(Collection<Sku> skus) {
		Map<Sku, Integer> inventories = new HashMap<Sku, Integer>();
        //Get the current sku quantity available
        Map<UUID, Integer> skusMap = skuDAO.findMapSkuIdQuantityAvailable(skus);
        for (Sku sku : skus) {
        	Set<UUID> keySet = skusMap.keySet();
        	if (keySet.contains(sku.getId())) {   
            	if (CommonUtil.nullSafeIntegerToPrimitive(sku.getQuantityAvailable()) !=  CommonUtil.nullSafeIntegerToPrimitive(skusMap.get(sku.getId()))) {
            		skuDAO.refresh(sku);
            	}
            } else {
        	   throw new ProductNotFoundException("The Sku with id " + sku.getId().toString() + " does not exists.");
            }		        	
            if (sku.isAvailable()) {
                if (InventoryType.CHECK_QUANTITY.equals(sku.getInventoryType())) {
                    if (sku.getQuantityAvailable() == null) {
                        inventories.put(sku, 0);
                    }
                    inventories.put(sku, sku.getQuantityAvailable());
                } else if (sku.getInventoryType() == null || InventoryType.ALWAYS_AVAILABLE.equals(sku.getInventoryType())) {
                    inventories.put(sku, null);
                } else {
                    inventories.put(sku, 0);
                }
            } else {
                inventories.put(sku, 0);
            }
        }

        return inventories;
		
	}
	
    @Override
    public Integer retrieveQuantityAvailable(Sku sku) {
        return retrieveQuantitiesAvailable(Arrays.asList(sku)).get(sku);
    }
	
	
	@Override
	public boolean isAvailable(Sku sku) {
		return isQuantityAvailable(sku, 1);
	}

	@Override
	public boolean isQuantityAvailable(Sku sku, int quantity) {
		if (sku == null) return false;
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity " + quantity + " is not valid. Must be greater than zero.");
        }
        if (!sku.isAvailable()) return false;
		InventoryType it;
		if (sku.isDefaultSku()) {
			it = sku.getInventoryType();
		} else {
			it = sku.lookupDefaultSku().getInventoryType();		
		}
	    if (InventoryType.CHECK_QUANTITY.equals(it)) {
            Integer quantityAvailable = retrieveQuantityAvailable(sku);
            return quantityAvailable != null && quantity <= quantityAvailable;
	    } else if (InventoryType.ALWAYS_AVAILABLE.equals(it)) {
	    	return true;
	    }
		return true;		
	}

}
