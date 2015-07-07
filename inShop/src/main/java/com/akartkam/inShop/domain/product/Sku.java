package com.akartkam.inShop.domain.product;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;

@Entity
@Table(name = "sku")
public class Sku extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6933254570165053658L;
	private String name;
	private String skuCode;
	private String description;
	//цена продажи(старая цена), новая цена(если есть), себестоимость
	private BigDecimal retailPrice, salePrice, costPrice;
}
