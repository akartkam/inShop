package com.akartkam.inShop.app;



import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.exception.InventoryUnavailableException;


public class Common {

	public static void main(String[] args) throws InventoryUnavailableException {
		Sku sku = new Sku();
		throw new InventoryUnavailableException("The referenced Sku " + sku.getId() + " is marked as unavailable, or an insufficient amount",
                sku.getId(), 5, 4);

	
	}

}
