package com.akartkam.inShop.formbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemsForJSON implements Serializable {
	
	private List<SkuForJSON> items = new ArrayList<SkuForJSON>();
	private Long totalItemsCount;
	
	public ItemsForJSON() {};
	
	public ItemsForJSON(List<SkuForJSON> skus, Long totalItemsCount) {
		this.items = skus;
		this.totalItemsCount = totalItemsCount;
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

	public Long getTotalItemsCount() {
		return totalItemsCount;
	}

	public void setTotalItemsCount(Long totalItemsCount) {
		this.totalItemsCount = totalItemsCount;
	}
	
	

}
