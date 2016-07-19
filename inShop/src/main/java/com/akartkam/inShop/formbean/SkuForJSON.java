package com.akartkam.inShop.formbean;

import java.math.BigDecimal;
import java.util.UUID;

public class SkuForJSON {
	private UUID id;
	private String name;
	private String images[];
	private String code;
	private String brand;
	private String model;
	private String description;
	private BigDecimal retailPrice, salePrice;
	private Integer quantityAvailable;
	private String productStatus[];
	private String optionValues[];
	
	public SkuForJSON(){};
	
	public SkuForJSON(UUID id,
			          String name, 
					  String images[],
					  String code,
					  String brand,
					  String model,
					  String description,
					  BigDecimal retailPrice, 
					  BigDecimal salePrice,
					  Integer quantityAvailable,
					  String productStatus[],
					  String optionValues[]){
		  this.id = id;
		  this.name = name; 
		  this.images = images;
		  this.code = code;
		  this.brand = brand;
		  this.model = model;
		  this.description = description; 
		  this.retailPrice = retailPrice;  
		  this.salePrice = salePrice;
		  this.quantityAvailable = quantityAvailable;
		  this.productStatus = productStatus; 
		  this.optionValues = optionValues;
	};
	public String[] getOptionValues() {
		return optionValues;
	}

	public void setOptionValues(String[] optionValues) {
		this.optionValues = optionValues;
	}

	public Integer getQuantityAvailable() {
		return quantityAvailable;
	}

	public void setQuantityAvailable(Integer quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public UUID getId() {
		return id;
	}
	
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
	public BigDecimal getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
	public String[] getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(String[] productStatus) {
		this.productStatus = productStatus;
	}

}
