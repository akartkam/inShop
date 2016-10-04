package com.akartkam.inShop.service.order;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.controller.order.CartController;
import com.akartkam.inShop.dao.product.SkuDAO;
import com.akartkam.inShop.domain.product.InventoryType;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.exception.ProductNotFoundException;
import com.akartkam.inShop.exception.InventoryUnavailableException;
import com.akartkam.inShop.util.CommonUtil;

@Service("InventoryService")
public class InventoryServiceImpl implements InventoryService {
	
	private static final Log LOG = LogFactory.getLog(InventoryServiceImpl.class);

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
	    if (InventoryType.CHECK_QUANTITY.equals(sku.getInventoryType())) {
            Integer quantityAvailable = retrieveQuantityAvailable(sku);
            return quantityAvailable != null && quantity <= quantityAvailable;
	    } else if (InventoryType.ALWAYS_AVAILABLE.equals(sku.getInventoryType())) {
	    	return true;
	    }
		return true;		
	}

	@Override
	@Transactional( rollbackFor={InventoryUnavailableException.class})
	public void decrementInventory(Map<Sku, Integer> skuQuantities) throws InventoryUnavailableException {
		Map<Sku, Integer> skuQuantAvailable = retrieveQuantitiesAvailable(skuQuantities.keySet());
        for (Entry<Sku, Integer> entry : skuQuantities.entrySet()) {
            Sku sku = entry.getKey();
            Integer quantity = entry.getValue();
            if (quantity == null || quantity < 1) {
                throw new IllegalArgumentException("Quantity " + quantity + " is not valid. Must be greater than zero and not null.");
            }
            if (sku.isAvailable()) {
            	if (InventoryType.CHECK_QUANTITY.equals(sku.getInventoryType())) {
            		Integer inventoryAvailable = skuQuantAvailable.get(sku);
                    if (inventoryAvailable == null) {
                        return;
                    }
                    if (inventoryAvailable < quantity) {
                        throw new InventoryUnavailableException(
                                "There was not enough inventory to fulfill this request.", sku.getId(), quantity, inventoryAvailable);
                    }
                    int newInventory = inventoryAvailable - quantity;
                    sku.setQuantityAvailable(newInventory);
                    skuDAO.update(sku);                    
            	} else {
            		LOG.info("Not decrementing inventory as the Sku has been marked as always available");
            	}
            } else {
                throw new InventoryUnavailableException("The Sku has been marked as unavailable", sku.getId(), quantity, 0);
            }
        } 
		
	}

	@Override
	@Transactional
	public void incrementInventory(Map<Sku, Integer> skuQuantities) {
		Map<Sku, Integer> skuQuantAvailable = retrieveQuantitiesAvailable(skuQuantities.keySet());
        for (Entry<Sku, Integer> entry : skuQuantities.entrySet()) {
            Sku sku = entry.getKey();
            Integer quantity = entry.getValue();
            if (quantity == null || quantity < 1) {
                throw new IllegalArgumentException("Quantity " + quantity + " is not valid. Must be greater than zero and not null.");
            }
        	if (InventoryType.CHECK_QUANTITY.equals(sku.getInventoryType())) {
        		Integer inventoryAvailable = skuQuantAvailable.get(sku);
                if (inventoryAvailable == null) {
                    return;
                }
                int newInventory = inventoryAvailable + quantity;
                sku.setQuantityAvailable(newInventory);
                skuDAO.update(sku);                    
        	} else {
        		LOG.info("Not incrementing inventory as the Sku has been marked as always available");
        	}
        } 
	}
	
	@Override
	public void incrementInventory(Sku sku, int quant) {
		Map<Sku, Integer> qMap = new HashMap<Sku, Integer>();
		qMap.put(sku, Integer.valueOf(quant));
		incrementInventory(qMap);
	}

	@Override
	public void decrementInventory(Sku sku, int quant) throws InventoryUnavailableException {
		Map<Sku, Integer> qMap = new HashMap<Sku, Integer>();
		qMap.put(sku, Integer.valueOf(quant));	
		decrementInventory(qMap);
	}
	
	
}
