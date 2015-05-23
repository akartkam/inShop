package com.akartkam.inShop.domain.product.attribute;

import java.io.Serializable;
import java.util.Set;

public interface Selectable<T extends Serializable> {
	Set<T> getItems();
	Set<String> getStringItems();
	void setStringItems(Set<String> items);

}
