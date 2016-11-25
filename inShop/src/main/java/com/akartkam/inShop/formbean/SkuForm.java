package com.akartkam.inShop.formbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;

public class SkuForm extends Sku {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4368976934463748993L;
    private List<ProductOptionValue> productOptionValuesList = new ArrayList<ProductOptionValue>();
    private List<ProductOption> productOptionsList = new ArrayList<ProductOption>();
	public SkuForm() {
		
	}

	public SkuForm(Sku sku) {
		if (sku != null) {
			this.setId(sku.getId());
			this.setActiveEndDate(sku.getActiveEndDate());
			this.setActiveStartDate(sku.getActiveStartDate());
			this.setCode(sku.getCode());
			this.setCostPrice(sku.getCostPrice());
			this.setDescription(sku.getDescription());
			this.setEnabled(sku.isEnabled());
			this.setImages(sku.getImages());
			this.setLongDescription(sku.getLongDescription());
			if (sku.getName() == null || "".equals(sku.getName())) {
				this.setName(sku.getProduct().getDefaultSku().getName());
			} else {
				this.setName(sku.getName());				
			}
			this.setOrdering(sku.getOrdering());
			this.setProduct(sku.getProduct());
			this.setCreatedDate(sku.getCreatedDate());
			this.setQuantityAvailable(sku.getQuantityAvailable());
			this.setRetailPrice(sku.getRetailPrice());
			this.setSalePrice(sku.getSalePrice());
			this.setInventoryType(sku.getInventoryType());
			this.setProductOptionValues(sku.getProductOptionValues());
			this.productOptionValuesList = new ArrayList<ProductOptionValue>(sku.getProductOptionValues()); 
			this.productOptionsList = new ArrayList<ProductOption>(sku.getProduct().getProductOptions());
		}
	}
	
	@NotEmpty
	@AdminPresentation(tab=EditTab.ADDITIONAL)
	public List<ProductOptionValue> getProductOptionValuesList() {
		productOptionValuesList.removeAll(Collections.singletonList(null));
		return productOptionValuesList;
	}
	
	
	public void setProductOptionValuesFromList() {
		setProductOptionValues(new HashSet<ProductOptionValue>(getProductOptionValuesList()));
	}

	public List<ProductOption> getProductOptionsList() {
		return productOptionsList;
	}	
	

}
