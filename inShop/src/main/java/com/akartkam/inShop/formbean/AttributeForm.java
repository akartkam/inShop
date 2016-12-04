package com.akartkam.inShop.formbean;


import java.util.HashSet;
import java.util.Set;

import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AttributeType;
import com.akartkam.inShop.domain.product.attribute.Selectable;
import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;

public class AttributeForm extends AbstractAttribute {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8862249978701586923L;
	private AttributeType attributeType;
	private Set<String> items = new HashSet<String>(0);

	public AttributeForm() {};
		
	@AdminPresentation(tab=EditTab.CONTENT)
    public Set<String> getItems() {
		return items;
	}
	

	public void setItems(Set<String> items) {
		this.items =  items;
	}	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AttributeForm(AbstractAttribute attributeSource) {
		if (attributeSource != null) {
			this.setId(attributeSource.getId());
			this.setCode(attributeSource.getCode());
			this.setName(attributeSource.getName());
			this.setDescription(attributeSource.getDescription());
			this.setAttributeType(attributeSource.getAttributeType());
			this.setOrdering(attributeSource.getOrdering());
			this.setAttributeCategory(attributeSource.getAttributeCategory());
			this.setCreatedDate(attributeSource.getCreatedDate());
			this.setEnabled(attributeSource.isEnabled());
			this.setUnit(attributeSource.getUnit());
			this.setIsShowOnProductHeader(attributeSource.getIsShowOnProductHeader());
			this.setAttributeValuesHolder(attributeSource.getAttributeValuesHolder());
			if (attributeSource instanceof Selectable)
				this.setItems(((Selectable)attributeSource).getStringItems());
		}
	}
	
	@Override
	public AttributeType getAttributeType() {
		return attributeType;
	}


	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;

	}

}
