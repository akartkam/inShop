package com.akartkam.inShop.domain.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import javax.persistence.JoinColumn;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;

@Entity
@Table(name = "sku")
public class Sku extends AbstractDomainObjectOrdering {
	private static final Log LOG = LogFactory.getLog(Sku.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -6933254570165053658L;
	private String name;
	private String code;
	private String description;
	private String longDescription;
	private int quantityAvailable;
	private Set<ProductStatus> productStatus = new HashSet<ProductStatus>();
	private InventoryType inventoryType;
	private Date activeStartDate;
	private Date activeEndDate;
	private BigDecimal retailPrice, salePrice, costPrice;
	private Set<ProductOptionValue> productOptionValues = new HashSet<ProductOptionValue>();
    private List<String> images = new ArrayList<String>();
    private Product defaultProduct;   
    private Product product;
    //for form
    private List<ProductOptionValue> productOptionValuesList = new ArrayList<ProductOptionValue>();

    @AdminPresentation(tab=EditTab.MAIN)
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
	public void setCode(String skuCode) {
		this.code = skuCode;
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
	
    @ElementCollection
    @CollectionTable(name="lnk_sku_image")
    @OrderColumn(name="ordering")
    public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}	
	
	@Transient
	public int getQuantityAvailable() {
		return quantityAvailable;
	}
	public void setQuantityAvailable(int quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	
    @ElementCollection
    @CollectionTable(name="lnk_product_status")
	@Enumerated(EnumType.STRING)
    @Column(name = "pstatus", nullable = false)
	public Set<ProductStatus> getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(Set<ProductStatus> productStatus) {
		this.productStatus = productStatus;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "inventory_type")
	public InventoryType getInventoryType() {
		return inventoryType;
	}
	public void setInventoryType(InventoryType inventoryType) {
		this.inventoryType = inventoryType;
	}
	
	@DateTimeFormat(pattern="dd.MM.yyyy HH:mm")
    @Column(name = "active_start_date")
    @Index(name="sku_active_start_index")
	public Date getActiveStartDate() {
		return activeStartDate;
	}
	public void setActiveStartDate(Date activeStartDate) {
		this.activeStartDate = activeStartDate;
	}

	@DateTimeFormat(pattern="dd.MM.yyyy HH:mm")
	@Column(name = "active_end_date")
    @Index(name="sku_active_end_index")	
	public Date getActiveEndDate() {
		return activeEndDate;
	}
	public void setActiveEndDate(Date activeEndDate) {
		this.activeEndDate = activeEndDate;
	}
	
	@NumberFormat(style=Style.CURRENCY)
	@DecimalMin("0.01")	
	@Digits(fraction = 5, integer = 14)
    @Column(name = "retail_price", precision = 19, scale = 5)	
    @AdminPresentation(tab=EditTab.MAIN)
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
	
	@ManyToMany	
    @JoinTable(name = "lnk_sku_option_value", 
        joinColumns = @JoinColumn(name = "sku_id"), 
        inverseJoinColumns = @JoinColumn(name = "product_option_value_id"))
    @BatchSize(size = 50)	
	public Set<ProductOptionValue> getProductOptionValues() {
		return productOptionValues;
	}
	public void setProductOptionValues(Set<ProductOptionValue> productOptionValues) {
		this.productOptionValues = productOptionValues;
		this.productOptionValuesList = new ArrayList<ProductOptionValue>(productOptionValues);
	}
	
	@Transient
	public List<ProductOptionValue> getProductOptionValuesList() {
		return productOptionValuesList;
	}

	
	@ManyToOne
	@JoinColumn
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
    @OneToOne(optional = true)
    @JoinColumn(name = "default_product_id")
	public Product getDefaultProduct() {
		return defaultProduct;
	}
	public void setDefaultProduct(Product defaultProduct) {
		this.defaultProduct = defaultProduct;
	}
	
	@Transient
	public ProductOptionValue getProductOptionValueByPO(ProductOption po) {
		for (ProductOptionValue pov: getProductOptionValues()) {
			if (pov.getProductOption().equals(po)) return pov; 
		}
		return null;
	}
	
	@Override
	@Transient
	public Sku clone() throws CloneNotSupportedException {
		Sku sku = (Sku) super.clone();
		sku.setId(UUID.randomUUID());
		sku.setName(new String(getName()));
		sku.setCode(new String(getCode()));
		sku.setDescription(new String(getDescription()));
		sku.setLongDescription(new String(getLongDescription()));
		sku.setQuantityAvailable(getQuantityAvailable());
		sku.setProduct(getProduct());
		sku.setProductStatus(new HashSet<ProductStatus>(getProductStatus()));
		sku.setInventoryType(getInventoryType());
		sku.setActiveStartDate(getActiveStartDate() != null ? new Date(getActiveStartDate().getTime()) : null);
		sku.setActiveEndDate(getActiveEndDate() != null ? new Date(getActiveEndDate().getTime()): null);
		sku.setSalePrice(getSalePrice() != null ? new BigDecimal(getSalePrice().toPlainString()): null);
		sku.setCostPrice(getCostPrice() != null ? new BigDecimal(getCostPrice().toPlainString()): null);
		sku.setRetailPrice(getRetailPrice() != null ? new BigDecimal(getRetailPrice().toPlainString()): null);
		sku.setProductOptionValues(new HashSet<ProductOptionValue>(getProductOptionValues()));
		sku.setImages(new ArrayList<String>(getImages()));
		sku.setCreatedBy(null);
		sku.setCreatedDate(null);
		sku.setUpdatedBy(null);
		sku.setUpdatedDate(null);
		return sku;
	}	
	
}
