package com.akartkam.inShop.domain.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
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
	private Brand brand;
	private String model;
	private List<AbstractAttributeValue> attributeValues = new ArrayList<AbstractAttributeValue>();
	private String url;
    Map<String, String> images = new HashMap<String, String>();	


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
	
    @Column(name = "url")
    @Index(name="product_url_index", columnNames={"url"})	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@ManyToOne
	@JoinColumn
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	@Column(name = "model")
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	
    @ElementCollection
    @MapKeyColumn
    @Column(name="image_url")
    @CollectionTable(name="lnk_product_image")
    public Map<String, String> getImages() {
		return images;
	}
	public void setImages(Map<String, String> images) {
		this.images = images;
	}
	
	@OneToMany(mappedBy="product", cascade = CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public List<AbstractAttributeValue> getAttributeValues() {
		return attributeValues;
	}
	public void setAttributeValues(List<AbstractAttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
	}	
	
	public void addAttributeValue (AbstractAttributeValue attributeValue, AbstractAttribute attribute) {
		if (attributeValue == null) throw new IllegalArgumentException("Null attributeValue!");
		if (attribute == null) throw new IllegalArgumentException("Null attribute!");
		if (attribute.getAttributeType() != attributeValue.getAttributeValueType()) throw new IllegalArgumentException("The type of attribute and attributeValue is different!"); 
		attributeValue.setAttribute(attribute);
		attributeValue.setProduct(this);
		attributeValues.add(attributeValue);

	}

}
