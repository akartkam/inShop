package com.akartkam.inShop.domain.product.attribute;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("STRING")
public class AttributeString extends AbstractAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2699408191856568519L;

	@Override
	@Transient
	public AttributeType getAttributeType() {
		return AttributeType.STRING;
	}
	
}
