package com.akartkam.inShop.domain.product.option;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.Unit;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;

@Entity
@Table(name = "Product_Option")
@BatchSize(size=50)
public class ProductOption extends AbstractDomainObjectOrdering {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3553806634260571351L;
	private String name;
	private String label;
	private Boolean required;
	private Boolean useInSkuGeneration;
	private List<ProductOptionValue> productOptionValues = new ArrayList<ProductOptionValue>();
	private Unit unit; 
	
	@AdminPresentation(tab=EditTab.MAIN)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
    @Column(name = "unit")
    @Enumerated(EnumType.STRING)
    public Unit getUnit() {
    	return unit;
    }
    
    public void setUnit(Unit unit) {
    	this.unit = unit;
    }
	
	@AdminPresentation(tab=EditTab.MAIN)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "label")	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Column(name = "required")
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	
	@Column(name = "use_in_sku")
	public Boolean getUseInSkuGeneration() {
		return useInSkuGeneration;
	}
	public void setUseInSkuGeneration(Boolean useInSkuGeneration) {
		this.useInSkuGeneration = useInSkuGeneration;
	}
	
	@AdminPresentation(tab=EditTab.CONTENT)
	@Valid
	@OneToMany(mappedBy="productOption", cascade = CascadeType.ALL, orphanRemoval = true)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			  org.hibernate.annotations.CascadeType.DELETE})
	@OrderBy("ordering")
	@BatchSize(size = 20)    
	public List<ProductOptionValue> getProductOptionValues() {
		return productOptionValues;
	}
	public void setProductOptionValues(List<ProductOptionValue> productOptionValues) {
		this.productOptionValues = productOptionValues;
	}
	
	@Transient
	public ProductOptionValue getProductOptionValueById(UUID id) {
		if (id==null) return null;
		for (ProductOptionValue pov: getProductOptionValues()){
			if (id.equals(pov.getId())) {
				return pov;
			}
		}
		return null;
	}

	@Override
	@Transient
	public ProductOption clone() throws CloneNotSupportedException {
		ProductOption po = (ProductOption) super.clone();
		po.setId(UUID.randomUUID());
		po.setName(new String(getName()));
		po.setLabel(new String(getLabel()));
		po.setProductOptionValues(new ArrayList<ProductOptionValue>());
		po.setUseInSkuGeneration(getUseInSkuGeneration());
		po.setCreatedBy(null);
		po.setCreatedDate(null);
		po.setUpdatedBy(null);
		po.setUpdatedDate(null);
		return po;
	}
	
}
