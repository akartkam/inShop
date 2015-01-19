package com.akartkam.inShop.domain.product;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;

@Entity
@Table(name = "Product")
public class Product extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = -583044339566068826L;
	private String name;
	private Category category;
	private String manufacturer;
	private String model;
	private List<AbstractAttributeValue> attributeValues;
	

	@NotNull
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull
	@ManyToOne
	@JoinColumn(nullable=false)
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Column(name = "manuf")
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	@Column(name = "model")
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}	
	
	@OneToMany(mappedBy="product", cascade = CascadeType.ALL)
	public List<AbstractAttributeValue> getAttributeValues() {
		return attributeValues;
	}
	public void setAttributeValues(List<AbstractAttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
	}	
	
	public void addAttributeValue (AbstractAttributeValue attributeValue) {
		if (attributeValue == null) throw new IllegalArgumentException("Null attributeValue!");
		attributeValues.add(attributeValue);
		attributeValue.setProduct(this);
	}

}
