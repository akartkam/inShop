package com.akartkam.inShop.domain.product.attribute;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SecondaryTable;




@Entity
@DiscriminatorValue("SLIST")
@SecondaryTable(name = "Attribute_SList_Value")
public class AttributeSListValue extends AbstractAttributeValue {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -953260131910566356L;
	private SList attributeValue;
	
	@Override
	@ManyToOne
	@JoinColumn(table = "Attribute_SList_Value", name="attributeValue", nullable = false)
	public SList getAttributeValue() {
		return attributeValue;
	}
	
	public void setAttributeValue(SList attributeValue) {
		this.attributeValue = attributeValue;
	}

/*	@Override
	@ManyToOne(targetEntity=AttributeSList.class)
	@JoinColumn(table = "Attribute_SList_Value", nullable = false)
	public AbstractAttribute getAttribute() {
		return attribute;
	}*/

}
