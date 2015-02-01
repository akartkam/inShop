package com.akartkam.inShop.domain.product.attribute;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SecondaryTable;
import javax.persistence.Transient;




@Entity
@DiscriminatorValue("SLIST")
@SecondaryTable(name = "Attribute_SList_Value")
public class AttributeSListValue extends AbstractAttributeValue<SList> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -953260131910566356L;
	
	
	@Override
	@ManyToOne
	@JoinColumn(table = "Attribute_SList_Value", name="attributeValue", nullable = false)
	public SList getAttributeValue() {
		return attributeValue;
	}


	@Override
	@Transient
	public AttributeType getAttribueValueType() {
		return AttributeType.SLIST;
	}
	
}
