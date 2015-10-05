package com.akartkam.inShop.formbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;






import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.ProductStatus;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.attribute.AttributeDecimalValue;
import com.akartkam.inShop.domain.product.attribute.AttributeSListValue;
import com.akartkam.inShop.domain.product.attribute.AttributeStringValue;
import com.akartkam.inShop.domain.product.attribute.AttributeType;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.exception.ProductNotFoundException;

public class ProductForm extends Product {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4758849266803696007L;
    private Map<Integer, AttributeDecimalValue> decimalValMap = new HashMap<Integer, AttributeDecimalValue>(); 
    private Map<Integer, AttributeStringValue> stringValMap = new HashMap<Integer, AttributeStringValue>(); 
    private Map<Integer, AttributeSListValue> slistValMap = new HashMap<Integer, AttributeSListValue>(); 
    private List<ProductStatus> productStatus = new ArrayList<ProductStatus>();
    private List<ProductOption> productOptionsForForm = new ArrayList<ProductOption>();
    
    
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
			productStatus = new ArrayList<ProductStatus>(product.getDefaultSku().getProductStatus());
			productOptionsForForm = new ArrayList<ProductOption>(product.getProductOptions());
		}
	};
	
	public void copyValuesFrom(Product product) {
		
	}
	
	public List<ProductStatus> getProductStatus() {
		return productStatus;
	}

	public void setProductStatusFromList() {
        getDefaultSku().setProductStatus(new HashSet<ProductStatus>(productStatus));
	}
	
	

	public List<ProductOption> getProductOptionsForForm() {
		return productOptionsForForm;
	}

	public void setProductOptionFromList() {
		setProductOptions(new HashSet<ProductOption>(productOptionsForForm));
	}

	@SuppressWarnings("rawtypes")
	public void complementNecessaryAttributes() throws ClassNotFoundException, InstantiationException, IllegalAccessException {			 
			 List<AbstractAttribute> at = new ArrayList<AbstractAttribute>();
			 at = getCategory().getAllAttributes(at, true);
			 List<AbstractAttributeValue> av = getAttributeValues();
			 boolean needAdd;
			 for (AbstractAttribute cat : at) {
				 needAdd = true;
				 for (AbstractAttributeValue cav: av) {
					 if (cat.equals(cav.getAttribute())) {
						needAdd = false;
						break;
					 }
				 }
				 if (needAdd) {
					 addAttributeValue(cat);
				 }
			 }
	  }

		
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
