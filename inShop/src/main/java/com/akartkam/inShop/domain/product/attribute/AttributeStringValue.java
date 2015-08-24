package com.akartkam.inShop.domain.product.attribute;


import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SecondaryTable;
import javax.persistence.Transient;



@Entity
@DiscriminatorValue("STRING")
@SecondaryTable(name = "Attribute_String_Value")
public class AttributeStringValue extends AbstractAttributeValue<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6264533059253592563L;
	

	@Override
	@Column(table = "Attribute_String_Value", name="attributeValue", nullable = false)
	public String getValue() {
		return attributeValue;
	}


	@Override
	@Transient
	public AttributeType getAttributeValueType() {
		return AttributeType.STRING;
	}


	@Override
	public void setStringValue(String value) {
		setValue(value);
	}
	
}
