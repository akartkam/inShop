package com.akartkam.inShop.domain.product.attribute;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("INTEGER")
public class AttributeInt extends AbstractAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7044082801851122060L;

	@Override
	@Transient
	public AttributeType getAttributeType() {
		return AttributeType.INTEGER;
	}

}
