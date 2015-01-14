package com.akartkam.inShop.domain.product.attribute;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.akartkam.inShop.domain.product.Product;

@Entity
@Table(name = "Attribute_SList_Value")
public class AttributeSListValue extends AbstractAttributeValue {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -953260131910566356L;
	private SList attributeValue;
	private Product product;

	@Override
	@ManyToOne
	@JoinColumn(name="attributeValue")
	public SList getAttributeValue() {
		return attributeValue;
	}
	
	public void setAttributeValue(SList attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	@ManyToOne
	@JoinColumn
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}	

}
