package com.akartkam.inShop.domain.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import javax.persistence.JoinColumn;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
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
	private String code;
	private String description;
	private int quantityAvailable;
	private ProductStatus productStatus;
	private InventoryType inventoryType;
	private Date activeStartDate;
	private Date activeEndDate;
	private BigDecimal retailPrice, salePrice, costPrice;
	private Set<ProductOptionValue> productOptionValues = new HashSet<ProductOptionValue>();
    private List<String> images = new ArrayList<String>();	
    private Product product;
	
	@Column(name = "code")
	public String getCode() {
		return code;
	}
	public void setCode(String skuCode) {
		this.code = skuCode;
	}
	
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description", length = Integer.MAX_VALUE - 1)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	
	@Enumerated(EnumType.STRING)
	@Column(name = "prod_status")
	public ProductStatus getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(ProductStatus productStatus) {
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
	
    @Column(name = "active_start_date")
    @Index(name="sku_active_start_index")
	public Date getActiveStartDate() {
		return activeStartDate;
	}
	public void setActiveStartDate(Date activeStartDate) {
		this.activeStartDate = activeStartDate;
	}

    @Column(name = "active_end_date")
    @Index(name="sku_active_end_index")	
	public Date getActiveEndDate() {
		return activeEndDate;
	}
	public void setActiveEndDate(Date activeEndDate) {
		this.activeEndDate = activeEndDate;
	}
	
	@NotNull
	@NotEmpty
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

    @Column(name = "sale_price", precision = 19, scale = 5)
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

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
