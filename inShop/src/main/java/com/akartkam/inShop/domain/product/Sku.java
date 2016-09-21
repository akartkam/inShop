package com.akartkam.inShop.domain.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import javax.persistence.JoinColumn;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
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
	private Integer quantityAvailable;
	private Set<ProductStatus> productStatus = new HashSet<ProductStatus>();
	private InventoryType inventoryType;
	private Date activeStartDate;
	private Date activeEndDate;
	private BigDecimal retailPrice, salePrice, costPrice;
	private Set<ProductOptionValue> productOptionValues = new HashSet<ProductOptionValue>();
    private List<String> images = new ArrayList<String>();
    private Product defaultProduct;   
    private Product product;

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
	
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "long_description", length = Integer.MAX_VALUE - 1)
	public String getLongDescription() {
		return longDescription;
	}
    
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}		
	
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="lnk_sku_image")
    @OrderColumn(name="ordering")
    public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}	
	
	@Min(0)
	@Column(name = "quantity_avable")
	public Integer getQuantityAvailable() {
		return quantityAvailable;
	}
	public void setQuantityAvailable(Integer quantityAvailable) {
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
	
	@DateTimeFormat(pattern="${date.format}")
    @Column(name = "active_start_date")
    @Index(name="sku_active_start_index")
	public Date getActiveStartDate() {
		return activeStartDate;
	}
	public void setActiveStartDate(Date activeStartDate) {
		this.activeStartDate = activeStartDate;
	}

	@DateTimeFormat(pattern="${date.format}")
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
        BigDecimal tmpRetailPrice = getRetailPriceInternal();
        /*if (tmpRetailPrice == null) {
            throw new IllegalStateException("Retail price on Sku with id " + getId() + " was null");
        }*/
        return tmpRetailPrice;
	}
	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	@NumberFormat(style=Style.CURRENCY)
	@Digits(fraction = 5, integer = 14)
	@DecimalMin("0.01")
	@Column(name = "sale_price", precision = 19, scale = 5)
	public BigDecimal getSalePrice() {
		BigDecimal returnPrice = null;
		BigDecimal optionValueAdjustments = null;
        if (salePrice != null) {
            // We have an explicitly set sale price directly on this entity. We will not apply any adjustments
            returnPrice = new BigDecimal(salePrice.toPlainString());
        }
        if (returnPrice == null && hasDefaultSku()) {
            returnPrice = lookupDefaultSku().getSalePrice();
            optionValueAdjustments = getProductOptionValueAdjustments();
        }
        if (returnPrice == null) {
            return null;
        }        
        if (optionValueAdjustments != null) {
            returnPrice = returnPrice.add(optionValueAdjustments);
        }
        return returnPrice;
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
	
	@ManyToMany()
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
	
    @OneToOne(optional = true, mappedBy="defaultSku")
	public Product getDefaultProduct() {
		return defaultProduct;
	}
	public void setDefaultProduct(Product defaultProduct) {
		this.defaultProduct = defaultProduct;
	}
	
	@Transient
	public boolean isOnSale() {
		BigDecimal retailPrice = getRetailPrice();
		BigDecimal salePrice = getSalePrice();
        return (salePrice != null && salePrice != BigDecimal.ZERO && salePrice.compareTo(retailPrice) == -1);
    }
	
	@Transient
    public boolean hasDefaultSku() {
        return (product != null && product.getDefaultSku() != null && !getId().equals(product.getDefaultSku().getId()));
    }

	@Transient
	public Sku lookupDefaultSku() {
        if (product != null && product.getDefaultSku() != null) {
            return product.getDefaultSku();
        } else {
            return null;
        }
    }
	
	@Transient
	//check the basic availability of current sku
	public boolean isAvailable() {
		if (!isEnabled() || InventoryType.UNAVAILABLE.equals(getInventoryType())) return false;
		if (hasDefaultSku()) {
			if (InventoryType.UNAVAILABLE.equals(lookupDefaultSku().getInventoryType())) return false;
		} else {
			if (!getDefaultProduct().isCanSellWithoutOptions()) return false;			
		}
		return true;
	}
    
	@Transient
    public BigDecimal getProductOptionValueAdjustments() {
		BigDecimal optionValuePriceAdjustments = null;
        if (getProductOptionValues() != null) {
            for (ProductOptionValue value : getProductOptionValues()) {
                if (value.getPriceAdjustment() != null) {
                    if (optionValuePriceAdjustments == null) {
                        optionValuePriceAdjustments = value.getPriceAdjustment();
                    } else {
                        optionValuePriceAdjustments = optionValuePriceAdjustments.add(value.getPriceAdjustment());
                    }
                }
            }
        }
        return optionValuePriceAdjustments;
    }    
	
	@Transient
    public BigDecimal getPrice() {
        return isOnSale() ? getSalePrice() : getRetailPrice();
    }
	
	@Transient
    private BigDecimal getRetailPriceInternal() {
		BigDecimal returnPrice = null;
		BigDecimal optionValueAdjustments = null;       
		if (retailPrice != null) {
            returnPrice = new BigDecimal(retailPrice.toPlainString());
        }
        if (returnPrice == null && hasDefaultSku()) {
            // Otherwise, we'll pull the retail price from the default sku
            returnPrice = lookupDefaultSku().getRetailPrice();
            optionValueAdjustments = getProductOptionValueAdjustments();
        }
        if (returnPrice != null && optionValueAdjustments != null) {
            returnPrice = returnPrice.add(optionValueAdjustments);
        }        
        return returnPrice;
    }
	
	@Transient
	public String getCommaDelemitedPOVL() {
		String ret = "";
	    for (ProductOptionValue pov : productOptionValues) ret = ret + pov.getOptionValue() + ", ";
	    if (ret != "" && ret.trim().endsWith(",")) ret = ret.substring(0, ret.length()-2);
	    return ret;
	}
	

	@Override
	@Transient
	public Sku clone() throws CloneNotSupportedException {
		Sku sku = (Sku) super.clone();
		sku.setId(UUID.randomUUID());
		if (getName() != null) sku.setName(new String(getName()));
		if (getCode() != null) sku.setCode(new String(getCode()));
		if (getDescription() != null) sku.setDescription(new String(getDescription()));
		if (getLongDescription() != null) sku.setLongDescription(new String(getLongDescription()));
		sku.setProduct(getProduct());
		sku.setProductStatus(new HashSet<ProductStatus>(getProductStatus()));
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
