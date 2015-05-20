package com.akartkam.inShop.domain.product.attribute;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;



@Entity
@DiscriminatorValue("SLIST")
@Table
public class AttributeSList extends AbstractAttribute  {

	/**
	 * 
	 */

	private static final long serialVersionUID = 3284620229615890714L;

    
	@Override
	@Transient
	public AttributeType getAttributeType() {
		return AttributeType.SLIST;
	}

	@Override
    @ElementCollection
    @Column(name="item", nullable = false)
    @OrderBy("item")
    @CollectionTable(name="items_attributeslist")
    public Set<String> getItems() {
		return items;
	}
	

	@Override
	public void setItems(Set<String> items) {
		this.items =  items;
	}
	
	public AttributeSList clone() throws CloneNotSupportedException {
		AttributeSList attrib = (AttributeSList) super.clone();
		attrib.setItems(new HashSet<String>(getItems()));
		return attrib;
	}

}
