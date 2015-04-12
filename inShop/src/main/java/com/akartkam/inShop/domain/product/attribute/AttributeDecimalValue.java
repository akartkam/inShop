package com.akartkam.inShop.domain.product.attribute;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.SecondaryTable;
import javax.persistence.Transient;



@Entity
@DiscriminatorValue("DECIMAL")
@SecondaryTable(name = "Attribute_Decimal_Value")
public class AttributeDecimalValue extends AbstractAttributeValue<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 354124918834557753L;



	@Override
	@Column(table = "Attribute_Decimal_Value", name= "attributeValue", nullable = false)
	public Double getAttributeValue() {
		return attributeValue;
	}



	@Override
	@Transient
	public AttributeType getAttributeValueType() {
		return AttributeType.DECIMAL;
	}

	
}
