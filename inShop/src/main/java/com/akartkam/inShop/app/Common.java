package com.akartkam.inShop.app;

import com.akartkam.inShop.exception.InventoryUnavailableException;


public class Common {

	public static void main(String[] args) throws InventoryUnavailableException {
        String url = "qwerty";
        String[] splited = url.split("/");
        System.out.println(splited.length);
        System.out.println(splited[0]);
	
	}

}
