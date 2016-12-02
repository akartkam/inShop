package com.akartkam.inShop.service.extension;

import com.akartkam.inShop.domain.product.Product;

public interface ProductDisplayNameModificator {
	void setProduct(final Product product);
	String getModifyedDisplayName();
	String getModifyedLongDisplayName();
}
