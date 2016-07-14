package com.akartkam.inShop.formbean;

public class SkuForJSON {
	private String name;
	private String images[];
	private String code;
	private String brand;
	private String model;
	private String description;
	private String retailPrice, salePrice;
	private String productStatus[];
	
	public SkuForJSON(){};
	
	public SkuForJSON(String name, 
					  String images[],
					  String code,
					  String brand,
					  String model,
					  String description,
					  String retailPrice, 
					  String salePrice,
					  String productStatus[]){
		  this.name = name; 
		  this.images = images;
		  this.code = code;
		  this.brand = brand;
		  this.model = model;
		  this.description = description; 
		  this.retailPrice = retailPrice;  
		  this.salePrice = salePrice;
		  this.productStatus = productStatus; 		
	};
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] images) {
		this.images = images;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(String retailPrice) {
		this.retailPrice = retailPrice;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public String[] getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(String[] productStatus) {
		this.productStatus = productStatus;
	}

}
