package com.akartkam.inShop.service.extension;

import com.akartkam.inShop.domain.product.Sku;

public interface ProductDisplayNameModificator {
	void setSku(final Sku sku);
	String getModifyedDisplayName(String name);
	String getModifyedLongDisplayName(String name);
}
