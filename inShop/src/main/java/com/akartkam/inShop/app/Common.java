package com.akartkam.inShop.app;

import java.util.UUID;

import com.akartkam.inShop.formbean.CartItemForm;

public class Common {

	public static void main(String[] args) {
		String pId = UUID.randomUUID().toString();
		String sId = UUID.randomUUID().toString();
		CartItemForm ci1 = new CartItemForm();
		CartItemForm ci2 = new CartItemForm();
		ci1.setProductId(pId);
		ci1.setSkuId(sId);
		ci2.setProductId(pId);
		ci2.setSkuId(sId);
		
		System.out.println("pId - "+pId +" ; sId - "+sId);
		System.out.println(ci1.hashCode());
		System.out.println(ci2.hashCode());
		System.out.println(ci1.equals(ci2));
	
	}

}
