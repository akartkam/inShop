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

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.akartkam.com.presentation.admin.AdminPresentation;
import com.akartkam.com.presentation.admin.EditTab;
import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;


@Entity
@Table(name = "Product_Option_Value")
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
	
	@AdminPresentation(tab=EditTab.CONTENT)
	@NotEmpty
	@Column(name="option_value")
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(final String optionValue) {
		this.optionValue = optionValue;
	}
	
	@AdminPresentation(tab=EditTab.CONTENT)
	//@Pattern(regexp="^\\-{0,1}[0-9]*\\.{0,1}[0-9]*$")
	@NumberFormat(style=Style.CURRENCY)
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
		pov.setPriceAdjustment(new BigDecimal(getPriceAdjustment().toString()));
		pov.setEnabled(isEnabled());
		pov.setProductOption(getProductOption());
		pov.setCreatedBy(null);
		pov.setCreatedDate(null);
		pov.setUpdatedBy(null);
		pov.setUpdatedDate(null);
		return pov;
	}
    
    
	

}
