package com.akartkam.inShop.domain.product.attribute;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.akartkam.inShop.domain.AbstractDomainObject;

@Entity
@Table(name = "Attribute_Decimal_Value")
public class AttributeDecimalValue extends AbstractDomainObject implements AttributeValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 354124918834557753L;
	private Double attributeValue;
	private AbstractAttribute attributeDecimal;

	@Column(name="attributeValue")
	public Double getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(Double attributeValue) {
		this.attributeValue = attributeValue;
	}

	@ManyToOne(targetEntity=AttributeDecimalValue.class)
	@JoinColumn
	public AbstractAttribute getAttributeDecimal() {
		return attributeDecimal;
	}

	public void setAttributeDecimal(AbstractAttribute attributeDecimal) {
		this.attributeDecimal = attributeDecimal;
	}
	
	
	
}
