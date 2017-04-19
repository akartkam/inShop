package com.akartkam.inShop.service.extension;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.akartkam.inShop.domain.Unit;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;

public class DefaultProductDisplayNameModificatorImpl implements
		ProductDisplayNameModificator {
	
	@Autowired(required=false)
	private MessageSource messageSource;
	private Set<String> allowedCategoriesId = new HashSet<String>();
	private Sku sku;
	private Product product;
	
	
	public Set<String> getAllowedCategoriesId() {
		return allowedCategoriesId;
	}

	public void setAllowedCategoriesId(Set<String> allowedCategoriesId) {
		this.allowedCategoriesId = allowedCategoriesId;
	}
	
	

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@SuppressWarnings("rawtypes")
	private String doModifyString(String str) {
		StringBuilder ret = new StringBuilder();
		String code = null;
		code = sku.lookupCode(); 
		if (code != null && !"".equals(code.trim())) {
			ret.append(code).append(" ");
		}		
		ret.append(str);
		Product product = sku.lookupProduct();
		if(product.getCategory().getShowQuanPerPackOnProductHeader()) {
			ret.append(",&nbsp;").append(sku.getQuantityPerPackage());
			if (messageSource != null) {
				ret.append("&nbsp;").append(messageSource.getMessage("product.default.pricePieceUnit", null, Locale.getDefault()));
			}				
		}
		for (AbstractAttributeValue av: product.getAttributeValues()) {
			if (av.getAttribute().getIsShowOnProductHeader()) {
				if (av.getStringValue() != null && !"".equals(av.getStringValue())) {
					ret.append(", <span class='product-header-attribute'>").append(av.getStringValue()).append("</span>");					
				}
				if (av.getAttribute().getUnit() != null) {
					String unit;
					if (messageSource != null) {
						unit = messageSource.getMessage("unit."+av.getAttribute().getUnit().name(), null, Locale.getDefault());
					} else {
						unit = av.getAttribute().getUnit().getFullNameR();
					}
					ret.append("<span class='unit'>").append(unit).append("</span>");
				}
			}
		}
		for (AbstractAttributeValue av: sku.getAttributeValues()) {
			if (av.getStringValue() != null && !"".equals(av.getStringValue())) {
				ret.append(", <span class='product-header-attribute'>").append(av.getStringValue()).append("</span>");					
			}
			if (av.getAttribute().getUnit() != null) {
				String unit;
				if (messageSource != null) {
					unit = messageSource.getMessage("unit."+av.getAttribute().getUnit().name(), null, Locale.getDefault());
				} else {
					unit = av.getAttribute().getUnit().getFullNameR();
				}
				ret.append("<span class='unit'>").append(unit).append("</span>");
			}
			
		}
		Set<ProductOptionValue> spov = sku.getProductOptionValues();
		if (spov.size()>0) {
			for (ProductOptionValue pov : spov) {
				if (pov.getOptionValue() != null && !"".equals(pov.getOptionValue())) {
					ret.append(", <span class='product-header-attribute'>").append(pov.getOptionValue()).append("</span>");
					if (pov.getProductOption().getUnit() != null) {
						String unit;
						if (messageSource != null) {
							unit = messageSource.getMessage("unit."+pov.getProductOption().getUnit().name(), null, Locale.getDefault());
						} else {
							unit = pov.getProductOption().getUnit().getFullNameR();
						}
						ret.append("<span class='unit'>").append(unit).append("</span>");	
					}
					
				}
			}
		}
		return ret.toString();		
	}
	
	private String doModifyLongString(String str) {
		StringBuilder ret = new StringBuilder();
		Product prod =product;
		if (prod == null) prod = sku.lookupProduct();
		List<String> sc = prod.getSkuCodes(); 
		if (sc.size()<=4) {
			ret.append(sc.toString());
		}
		ret.append(str);
		return ret.toString();
	}

	@Override
	public void setSku(final Sku sku) {
		this.sku = sku;
	}

	@Override
	public String getModifyedDisplayName(String name) {
		if (sku == null) return null;
		Product product = sku.lookupProduct();
		if (!allowedCategoriesId.contains(product.getCategory().getId().toString())) return name;
		return doModifyString(name);
	}
	public String getModifyedLongDisplayName(String name) {
		if (sku == null) return null;
		Product product = sku.lookupProduct();
		if (!allowedCategoriesId.contains(product.getCategory().getId().toString())) return name;
		return doModifyLongString(name);
	}

}
