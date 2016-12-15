package com.akartkam.inShop.service.extension;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;

public class DefaultProductDisplayNameModificatorImpl implements
		ProductDisplayNameModificator {
	
	@Autowired(required=false)
	private MessageSource messageSource;
	private Set<String> allowedCategoriesId = new HashSet<String>();
	private Product product;
	
	
	public Set<String> getAllowedCategoriesId() {
		return allowedCategoriesId;
	}

	public void setAllowedCategoriesId(Set<String> allowedCategoriesId) {
		this.allowedCategoriesId = allowedCategoriesId;
	}

	@SuppressWarnings("rawtypes")
	private String doModifyString(String str) {
		StringBuilder ret = new StringBuilder();
		if (product.getDefaultSku().getCode() != null && !"".equals(product.getDefaultSku().getCode())) {
			ret.append(product.getDefaultSku().getCode()).append(" ");
		}		
		ret.append(str);
		for (AbstractAttributeValue av: product.getAttributeValues()) {
			if(product.getCategory().getShowQuanPerPackOnProductHeader()) {
				ret.append(",&nbsp;").append(product.getDefaultSku().getQuantityPerPackage());
				if (messageSource != null) {
					ret.append("&nbsp;").append(messageSource.getMessage("product.default.pricePieceUnit", null, Locale.getDefault()));
				}				
			}
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
		return ret.toString();		
	}

	@Override
	public void setProduct(final Product product) {
		this.product = product;
	}

	@Override
	public String getModifyedDisplayName(String name) {
		if (product == null) return null;
		if (!allowedCategoriesId.contains(product.getCategory().getId().toString())) return name;
		return doModifyString(name);
	}
	public String getModifyedLongDisplayName(String name) {
		if (product == null) return null;
		if (!allowedCategoriesId.contains(product.getCategory().getId().toString())) return name;
		return doModifyString(name);
	}

}
