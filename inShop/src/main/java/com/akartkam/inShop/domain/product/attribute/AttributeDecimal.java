package com.akartkam.inShop.domain.product.attribute;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
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
	public AttributeType getAttribueType() {
		return AttributeType.DECIMAL;
	}
	
	public void setAttribueType(AttributeType attributeType){};

	/*@Override
	@OneToMany(mappedBy="attribute", targetEntity=AttributeDecimalValue.class)
	public List<AbstractAttributeValue> getAttributeValues() {
		return attributeValues;
	}
*/
	

}
