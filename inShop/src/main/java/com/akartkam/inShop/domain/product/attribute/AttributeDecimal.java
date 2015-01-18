package com.akartkam.inShop.domain.product.attribute;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("DECIMAL")
@Table(name = "Attribute_Decimal")
public class AttributeDecimal extends AbstractAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5596242771906778351L;
	
	
	@Override
	@Transient
	public AttribueType getAttribueType() {
		return AttribueType.DECIMAL;
	}

	/*@Override
	@OneToMany(mappedBy="attribute", targetEntity=AttributeDecimalValue.class)
	public List<AbstractAttributeValue> getAttributeValues() {
		return attributeValues;
	}
*/
	

}
