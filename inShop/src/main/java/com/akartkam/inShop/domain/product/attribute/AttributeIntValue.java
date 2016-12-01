package com.akartkam.inShop.domain.product.attribute;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.SecondaryTable;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("INTEGER")
@SecondaryTable(name = "Attribute_Int_Value")
public class AttributeIntValue extends AbstractAttributeValue<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6588417399881087200L;

	@Override
	@Column(table = "Attribute_Int_Value", name= "attributeValue", nullable = false)
	public Integer getValue() {
		return attributeValue;
	}


	@Override
	public void setStringValue(String value) {
		setValue(Integer.parseInt(value));
	}

	@Override
	@Transient
	public AttributeType getAttributeValueType() {
		return AttributeType.INTEGER;
	}

}
