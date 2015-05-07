package com.akartkam.inShop.domain.product.attribute;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("STRING")
@Table
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
	
	
/*	@Override
	@OneToMany(mappedBy="attribute", targetEntity=AttributeStringValue.class)
	public List<AbstractAttributeValue> getAttributeValues() {
		return attributeValues;
	}*/

}
