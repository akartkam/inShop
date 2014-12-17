package com.akartkam.inShop.domain.product.attribute;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Attribute_Decimal")
public class AttributeDecimal extends AbstractAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5596242771906778351L;
	private List<AttributeValue> attributeValue;

	@Override
	public AttribueType getAttribueType() {
		return AttribueType.DECIMAL;
	}

	@Override
	@OneToMany(mappedBy="attributeDecimal", targetEntity=AttributeDecimalValue.class)
	public List<AttributeValue> getAttributeValue() {
		// TODO Auto-generated method stub
		return attributeValue;
	}

}
