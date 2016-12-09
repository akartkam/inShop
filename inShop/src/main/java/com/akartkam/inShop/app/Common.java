package com.akartkam.inShop.app;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.exception.InventoryUnavailableException;


public class Common {

	public static void main(String[] args) throws InventoryUnavailableException {
       Product p = new Product();
       System.out.println(p.urlPrefix);
       Sku s = new Sku(); 
       System.out.println(s.urlPrefix);
	}

}
