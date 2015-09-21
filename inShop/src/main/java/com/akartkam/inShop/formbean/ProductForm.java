package com.akartkam.inShop.formbean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;






import java.util.TreeMap;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.attribute.AttributeDecimalValue;
import com.akartkam.inShop.domain.product.attribute.AttributeSListValue;
import com.akartkam.inShop.domain.product.attribute.AttributeStringValue;
import com.akartkam.inShop.domain.product.attribute.AttributeType;

public class ProductForm extends Product {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4758849266803696007L;
    private Map<Integer, AttributeDecimalValue> decimalValMap = new HashMap<Integer, AttributeDecimalValue>(); 
    private Map<Integer, AttributeStringValue> stringValMap = new HashMap<Integer, AttributeStringValue>(); 
    private Map<Integer, AttributeSListValue> slistValMap = new HashMap<Integer, AttributeSListValue>(); 
	
 
	public ProductForm() {};
	
	public ProductForm(Product product) {
		if (product != null) {
			this.setId(product.getId());
			this.setAdditionalSku(product.getAdditionalSku());
			this.setBrand(product.getBrand());
			this.setAttributeValues(product.getAttributeValues());
			this.setCanSellWithoutOptions(product.isCanSellWithoutOptions());
			this.setCategory(product.getCategory());
			this.setDefaultSku(product.getDefaultSku());
			this.setEnabled(product.isEnabled());
			this.setModel(product.getModel());
			this.setOrdering(product.getOrdering());
			this.setProductOptions(product.getProductOptions());
			this.setUrl(product.getUrl());
			setMapAttributeValues();
		}
	};

	@SuppressWarnings("rawtypes")
	public void setMapAttributeValues() {
		List<AbstractAttributeValue> attributeValues = this.getAttributeValues();
		if (attributeValues != null) {
			for (int i=0; i<attributeValues.size();i++) {
				if (AttributeType.DECIMAL.equals(attributeValues.get(i).getAttribute().getAttributeType())) {
					decimalValMap.put(i, (AttributeDecimalValue) attributeValues.get(i));	
				} else
				if (AttributeType.STRING.equals(attributeValues.get(i).getAttribute().getAttributeType())) {
					stringValMap.put(i, (AttributeStringValue) attributeValues.get(i));
				} else
				if (AttributeType.SLIST.equals(attributeValues.get(i).getAttribute().getAttributeType())) { 
					slistValMap.put(i, (AttributeSListValue) attributeValues.get(i));
				}
			}  
			   
		}		
	}
	
	@SuppressWarnings("rawtypes")
	public void setAttributeValuesFromMap() {
		Map<Integer, AbstractAttributeValue> fullMap = new TreeMap<Integer, AbstractAttributeValue>();
		fullMap.putAll(getDecimalValMap());
		fullMap.putAll(getStringValMap());
		fullMap.putAll(getSlistValMap());
		this.getAttributeValues().clear();
		this.getAttributeValues().addAll(fullMap.values());
	}

	public Map<Integer, AttributeDecimalValue> getDecimalValMap () {
		return decimalValMap;
	}

	public Map<Integer, AttributeStringValue> getStringValMap() {
		return stringValMap;
	}

	public Map<Integer, AttributeSListValue> getSlistValMap() {
		return slistValMap;
	}
	
	
	
}
