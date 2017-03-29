package com.akartkam.inShop.domain.product.option;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.formatter.CurrencyFormat;
import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;


@Entity
@Table(name = "Product_Option_Value")
@BatchSize(size=50)
public class ProductOptionValue extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4079321298055866701L;
	private ProductOption productOption;
	private String optionValue;
    private BigDecimal priceAdjustment;

    @ManyToOne
    @JoinColumn
    public ProductOption getProductOption() {
		return productOption;
	}
	public void setProductOption(ProductOption productOption) {
		this.productOption = productOption;
	}
	
	@NotEmpty
	@Column(name="option_value")
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(final String optionValue) {
		this.optionValue = optionValue;
	}
	
	@CurrencyFormat
	@Digits(fraction = 5, integer = 14)
    @Column(name = "price_adjustment", precision = 19, scale = 5)
	public BigDecimal getPriceAdjustment() {
		return priceAdjustment;
	}
	public void setPriceAdjustment(BigDecimal priceAdjustment) {
		this.priceAdjustment = priceAdjustment;
	}
	
	@Override
	public boolean canRemove() {
		return super.canRemove();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		ProductOptionValue pov = (ProductOptionValue) super.clone();
		pov.setId(UUID.randomUUID());
		pov.setOptionValue(new String(getOptionValue()));
		pov.setPriceAdjustment(getPriceAdjustment() != null? new BigDecimal(getPriceAdjustment().toString()): null);
		pov.setEnabled(isEnabled());
		pov.setProductOption(getProductOption());
		pov.setCreatedBy(null);
		pov.setCreatedDate(null);
		pov.setUpdatedBy(null);
		pov.setUpdatedDate(null);
		return pov;
	}
    
    
	

}
