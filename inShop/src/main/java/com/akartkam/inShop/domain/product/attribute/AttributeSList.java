package com.akartkam.inShop.domain.product.attribute;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("SLIST")
@Table
public class AttributeSList extends AbstractAttribute {

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

		
/*	@Override
	@OneToMany(mappedBy="attribute", targetEntity=AttributeSListValue.class)
	public List<AbstractAttributeValue> getAttributeValues() {
		return attributeValues;
	}*/

	
    @ElementCollection
    @Column(name="item", nullable = false)
    @OrderBy("item")
    @CollectionTable(name="items_slist")
    public Set<String> getItems() {
		return items;
	}
	public void setItems(Set<String> items) {
		this.items = items;
	}	
	


}
