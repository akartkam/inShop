package com.akartkam.inShop.domain.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.akartkam.com.presentation.admin.AdminPresentation;
import com.akartkam.com.presentation.admin.EditTab;
import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.option.ProductOption;

@Entity
@Table(name = "Product")
@SuppressWarnings("rawtypes")
public class Product extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = -583044339566068826L;
	private String name;
	private String code;
	private Category category;
	private String description;
	private String longDescription;
	private Brand brand;
	private String model;
	private List<AbstractAttributeValue> attributeValues = new ArrayList<AbstractAttributeValue>();
	private String url;
    private List<String> images = new ArrayList<String>();	
    private Set<ProductOption> productOptions = new HashSet<ProductOption>(0);
    private Boolean canSellWithoutOptions = true;
	//цена продажи(старая цена), новая цена(если есть), себестоимость
	private BigDecimal retailPrice, salePrice, costPrice;
	
    @AdminPresentation(tab=EditTab.MAIN)
    @NotNull
	@NotEmpty
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
//	@NotNull
//	@NotEmpty
	@NumberFormat(style=Style.CURRENCY)
	@Digits(fraction = 5, integer = 14)
	@DecimalMin("0.01")
    @Column(name = "retail_price", precision = 19, scale = 5)	
	public BigDecimal getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}
	
	@NumberFormat(style=Style.CURRENCY)
	@Digits(fraction = 5, integer = 14)
	@DecimalMin("0.01")
    @Column(name = "sale_price", precision = 19, scale = 5)
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	@NumberFormat(style=Style.CURRENCY)
	@Digits(fraction = 5, integer = 14)
	@DecimalMin("0.01")
    @Column(name = "cost_price", precision = 19, scale = 5)
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}	
	
	
	@Column(name = "can_sell_without_options")
	public Boolean isCanSellWithoutOptions() {
		return canSellWithoutOptions;
	}
	public void setCanSellWithoutOptions(Boolean canSellWithoutOptions) {
		this.canSellWithoutOptions = canSellWithoutOptions;
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
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "long_description", length = Integer.MAX_VALUE - 1)
	public String getLongDescription() {
		return longDescription;
	}
    
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
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
    @CollectionTable(name="lnk_product_image")
    @OrderColumn(name="ordering")
    public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
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
	
	@ManyToMany(cascade = CascadeType.ALL)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		      org.hibernate.annotations.CascadeType.DELETE})
	@JoinTable(name = "lnk_product_option", joinColumns = { 
			@JoinColumn(name = "product_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "product_option_id", 
					nullable = false, updatable = false) })
	public Set<ProductOption> getProductOptions() {
		return Collections.unmodifiableSet(productOptions);
	}
	
	public void setProductOptions(Set<ProductOption> productOption) {
		this.productOptions = productOption;
	}
	
    public void addProductOption(ProductOption productOption) {
        if (productOption == null) throw new IllegalArgumentException("Null productOption!");
        productOptions.add(productOption);
    }
    public void removeProductOption(ProductOption productOption) {
        if (productOption == null) throw new IllegalArgumentException("Null productOption!");
        productOptions.remove(productOption);
    }
    
	@Override
	@Transient
	public Product clone() throws CloneNotSupportedException {
		Product product = (Product) super.clone();
		return product;
	}    


}
