package com.akartkam.inShop.domain.product.attribute;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;



@Entity
@DiscriminatorValue("SLIST")
public class AttributeSList extends AbstractAttribute implements Selectable<String>  {

	/**
	 * 
	 */

	private static final long serialVersionUID = 3284620229615890714L;
	private Set<String> items = new HashSet<String>(0);

    
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
	

	public void setItems(Set<String> items) {
		this.items =  items;
	}
	
	public AttributeSList clone() throws CloneNotSupportedException {
		AttributeSList attrib = (AttributeSList) super.clone();
		attrib.setItems(new HashSet<String>(getItems()));
		return attrib;
	}

	//Into this method should convert items to Set<String> for AttributeForm
	@Transient
	@Override
	public Set<String> getStringItems() {
		return getItems();
	}

	@Override
	public void setStringItems(Set<String> items) {
		setItems(items);
	}

}
