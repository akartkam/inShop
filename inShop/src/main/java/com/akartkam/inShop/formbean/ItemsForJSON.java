package com.akartkam.inShop.formbean;

import java.util.ArrayList;
import java.util.List;

public class ItemsForJSON {
	
	private List<SkuForJSON> items = new ArrayList<SkuForJSON>();
	
	public ItemsForJSON() {};
	
	public ItemsForJSON(List<SkuForJSON> skus) {
		this.items = skus;
	}

	public List<SkuForJSON> getItems() {
		return items;
	}

	public void setSkus(List<SkuForJSON> skus) {
		this.items = skus;
	}
	
	public int getItemsCount () {
		return items.size();
	}

}
