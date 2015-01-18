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

import com.akartkam.inShop.domain.AbstractDomainObject;
import com.akartkam.inShop.domain.product.Product;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
						name = "DISCRIMINATOR",
						discriminatorType = DiscriminatorType.STRING
)
@Table(name = "Attribute_Value")
public abstract class AbstractAttributeValue extends AbstractDomainObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6088345502855127691L;
	protected AbstractAttribute attribute;
	protected Product product;
	
	@Transient
	public abstract Serializable getAttributeValue();
	
	@ManyToOne
	@JoinColumn(nullable = false)
	public AbstractAttribute getAttribute() {
		return attribute;
	}
	
	public void setAttribute(AbstractAttribute attribute) {
		this.attribute = attribute;
	}
	
	@ManyToOne
	@JoinColumn(name="product_id", nullable=false)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}


}
