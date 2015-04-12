package com.akartkam.inShop.formbean;

import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AttributeType;

public class AttributeForm extends AbstractAttribute {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8862249978701586923L;
	private AttributeType attributeType;


	public AttributeForm() {};
	
	public AttributeForm(AbstractAttribute attributeSource) {
		if (attributeSource != null) {
			this.setId(attributeSource.getId());
			this.setName(attributeSource.getName());
			this.setAttributeType(attributeSource.getAttributeType());
			this.setOrdering(attributeSource.getOrdering());
			this.setAttributeCategory(attributeSource.getAttributeCategory());
			this.setCreatedDate(attributeSource.getCreatedDate());
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
