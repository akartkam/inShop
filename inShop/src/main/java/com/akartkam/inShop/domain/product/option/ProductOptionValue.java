package com.akartkam.inShop.domain.product.option;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;

@Entity
@Table(name = "Product_Option_Value")
public class ProductOptionValue extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4079321298055866701L;
	private ProductOption ProductOption;
	private String optionValue;
    private BigDecimal priceAdjustment;

    @ManyToOne
    @JoinColumn
    public ProductOption getProductOption() {
		return ProductOption;
	}
	public void setProductOption(ProductOption productOption) {
		ProductOption = productOption;
	}
	
	@Column(name="option_value")
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
    @Column(name = "price_adjustment", precision = 19, scale = 5)
	public BigDecimal getPriceAdjustment() {
		return priceAdjustment;
	}
	public void setPriceAdjustment(BigDecimal priceAdjustment) {
		this.priceAdjustment = priceAdjustment;
	}
    
    
	

}
