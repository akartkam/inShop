package com.akartkam.inShop.domain.product.attribute;


import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.SecondaryTable;
import javax.persistence.Transient;




@Entity
@DiscriminatorValue("SLIST")
@SecondaryTable(name = "Attribute_SList_Value")
public class AttributeSListValue extends AbstractAttributeValue<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -953260131910566356L;
	
	
	@Override
	@Column(table = "Attribute_SList_Value", name="attributeValue", nullable = false)	
	public String getAttributeValue() {
		return attributeValue;
	}


	@Override
	@Transient
	public AttributeType getAttributeValueType() {
		return AttributeType.SLIST;
	}
	
}
