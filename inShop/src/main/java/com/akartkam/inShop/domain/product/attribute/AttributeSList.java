package com.akartkam.inShop.domain.product.attribute;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("SLIST")
@Table(name = "Attribute_Slist")
public class AttributeSList extends AbstractAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3284620229615890714L;
	private List<SList> sList;

	@Override
	public AttribueType getAttribueType() {
		return AttribueType.SLIST;
	}

	@Override
	@OneToMany(mappedBy="attribute", targetEntity=AttributeDecimalValue.class)
	public List<AbstractAttributeValue> getAttributeValues() {
		return attributeValues;
	}

	@OneToMany(mappedBy="attributeSList")
	public List<SList> getSList() {
		return sList;
	}

	public void setSList(List<SList> sList) {
		this.sList = sList;
	}	

}
