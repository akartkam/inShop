package com.akartkam.inShop.domain.product.attribute;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;

@Entity
@Table(name = "SList")
public class SList extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4644108764012656056L;
	private String value;
	private AttributeSList attributeSList;
	private List<AttributeSListValue> attributeSListValue;
	

	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne
	@JoinColumn(name="attribute_id", nullable = false)
	public AttributeSList getAttributeSList() {
		return attributeSList;
	}
	public void setAttributeSList(AttributeSList attributeSList) {
		this.attributeSList = attributeSList;
	}
	
	@OneToMany(mappedBy="attributeValue")
	public List<AttributeSListValue> getAttributeSListValue() {
		return attributeSListValue;
	}
	public void setAttributeSListValue(List<AttributeSListValue> attributeSListValue) {
		this.attributeSListValue = attributeSListValue;
	}	
	
}
