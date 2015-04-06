package com.akartkam.inShop.domain.product.attribute;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.product.Category;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
						name = "DISCRIMINATOR",
						discriminatorType = DiscriminatorType.STRING
)
@Table(name = "Attribute")
public abstract class AbstractAttribute extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1978623223082601133L;
	private String name;
	private AttributeCategory attributeCategory;
	private Set<Category> category;
	protected List<AbstractAttributeValue> attributeValues;
	

	@NotNull
	@Column(name = "name", unique=true, nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull
	@Transient
	public abstract AttributeType getAttribueType();

	/*@Transient
	public abstract List<AbstractAttributeValue> getAttributeValues();
	*/
	@OneToMany(mappedBy="attribute")
	public List<AbstractAttributeValue> getAttributeValues() {
		return attributeValues;
	}	
	
	public void setAttributeValues(List<AbstractAttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
	}
	
		
	@ManyToOne
	@JoinColumn
	public AttributeCategory getAttributeCategory() {
		return attributeCategory;
	}
	public void setAttributeCategory(AttributeCategory attributeCategory) {
		this.attributeCategory = attributeCategory;
	}	

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "lnk_category_attribute", joinColumns = { 
			@JoinColumn(name = "ATTRIBUTE_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "CATEGORY_ID", 
					nullable = false, updatable = false) })
	public Set<Category> getCategory() {
		return Collections.unmodifiableSet(category);
	}
	public void setCategory(Set<Category> category) {
		this.category = category;
	}	

}
