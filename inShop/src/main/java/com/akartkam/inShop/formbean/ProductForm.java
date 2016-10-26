package com.akartkam.inShop.formbean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.ProductStatus;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.option.ProductOption;


public class ProductForm extends Product {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4758849266803696007L;
    private List<ProductStatus> productStatus = new ArrayList<ProductStatus>();
    private List<ProductOption> productOptionsForForm = new ArrayList<ProductOption>();
    private String urlForForm;
    
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
			this.setCreatedDate(product.getCreatedDate());
			productStatus = new ArrayList<ProductStatus>(product.getDefaultSku().getProductStatus());
			productOptionsForForm = new ArrayList<ProductOption>(product.getProductOptions());
			buildShortUrl();
		}
	};
	
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
			 Category ct = getCategory();
			 if (ct == null) return;
			 at = ct.getAllAttributes(at, true);
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

	@NotEmpty
	@Pattern(regexp="^[a-z0-9-]*$", message="{error.bad.urlForForm}")
	public String getUrlForForm() {
		return urlForForm;
	}

	public void setUrlForForm(String urlForForm) {
		this.urlForForm = urlForForm;
	}

	private void buildShortUrl() {
		if (getUrl() != null && !"".equals(getUrl())) {
			String[] splitedUrl = getUrl().split("/"); 
			urlForForm = splitedUrl[splitedUrl.length-1];
		}	
	}	
	
	
}
