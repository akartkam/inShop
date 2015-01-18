package com.akartkam.inShop.domain.product.attribute;


import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SecondaryTable;



@Entity
@DiscriminatorValue("STRING")
@SecondaryTable(name = "Attribute_String_Value")
public class AttributeStringValue extends AbstractAttributeValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6264533059253592563L;
	private String attributeValue;

	@Override
	@Column(table = "Attribute_String_Value", name="attributeValue", nullable = false)
	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

/*	@Override
	@ManyToOne(targetEntity=AttributeString.class)
	@JoinColumn(table = "Attribute_String_Value", nullable = false)
	public AbstractAttribute getAttribute() {
		return attribute;
	}*/
	
	
	
}
