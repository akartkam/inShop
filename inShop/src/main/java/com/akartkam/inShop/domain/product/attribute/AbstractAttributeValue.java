package com.akartkam.inShop.domain.product.attribute;


import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

import com.akartkam.inShop.domain.AbstractDomainObject;
import com.akartkam.inShop.domain.product.Product;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
						name = "DISCRIMINATOR",
						discriminatorType = DiscriminatorType.STRING
)
@Table(name = "Attribute_Value")
public abstract class AbstractAttributeValue<T extends Serializable> extends AbstractDomainObject implements AttributeValue<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6088345502855127691L;
	protected AbstractAttribute attribute;
	protected Product product;
	protected T attributeValue;
	
	@Transient
	@Override
	public abstract T getValue();
	
	@Override
	public void setValue(T value) {
		attributeValue = value;
	}

	
	//Can override for specific convert object to string (used for SListValues)
	@Transient
	public String getStringValue() {
		return attributeValue.toString();
	}
	
	public abstract void setStringValue (String value);
	
	@NotNull
	@Transient
	public abstract AttributeType getAttributeValueType();
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@ForeignKey(name = "fk_attribute_id")
	public AbstractAttribute getAttribute() {
		return attribute;
	}
	
	public void setAttribute(AbstractAttribute attribute) {
		this.attribute = attribute;
	}
	
	@ManyToOne
	@JoinColumn(nullable=false)
	@ForeignKey(name = "fk_product_id")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}


}
