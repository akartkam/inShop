package com.akartkam.inShop.domain.product.attribute;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Attribute_String_Value")
public class AttributeStringValue extends AbstractAttributeValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6264533059253592563L;
	private String attributeValue;


	@Override
	@Column(name="attributeValue")
	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@Override
	@ManyToOne(targetEntity=AttributeString.class)
	@JoinColumn
	public AbstractAttribute getAttribute() {
		return attribute;
	}	
	
}
