package com.akartkam.inShop.domain.product.attribute;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Attribute_Decimal")
public class AttributeDecimal extends AbstractAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5596242771906778351L;
	private List<AttributeValue> attributeValues;
	
	@Override
	@Transient
	public AttribueType getAttribueType() {
		return AttribueType.DECIMAL;
	}

	@Override
	@OneToMany(mappedBy="attributeDecimal", targetEntity=AttributeDecimalValue.class)
	public List<AttributeValue> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(List<AttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
	}	

}
