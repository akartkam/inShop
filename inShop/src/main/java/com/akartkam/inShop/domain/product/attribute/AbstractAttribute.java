package com.akartkam.inShop.domain.product.attribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.hibernate.annotations.Cascade;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.product.Category;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
						name = "DISCRIMINATOR",
						discriminatorType = DiscriminatorType.STRING
)
@Table(name = "Attribute")
@SuppressWarnings("rawtypes")
public abstract class AbstractAttribute extends AbstractDomainObjectOrdering {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1978623223082601133L;
	private String name;
	private AttributeCategory attributeCategory;
	private Set<Category> category = new HashSet<Category>(0);
	protected List<AbstractAttributeValue> attributeValues = new ArrayList<AbstractAttributeValue>(0);

	
	
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "name", unique=true, nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull
	@Transient
	public abstract AttributeType getAttributeType();
	
	@OneToMany(mappedBy="attribute")
	public List<AbstractAttributeValue> getAttributeValues() {
		return attributeValues;
	}	
	
	public void setAttributeValues(List<AbstractAttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
	}
	
	
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	public AttributeCategory getAttributeCategory() {
		return attributeCategory;
	}
	public void setAttributeCategory(AttributeCategory attributeCategory) {
		this.attributeCategory = attributeCategory;
	}	

	@ManyToMany(mappedBy = "attributes", cascade = CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public Set<Category> getCategory() {
		return Collections.unmodifiableSet(category);
	}
	
	public void setCategory(Set<Category> category) {
		this.category = category;
	}
	
	@Transient
	public Collection<String> getStringAttributeValues() {
		return CollectionUtils.collect(getAttributeValues(), new Transformer<AbstractAttributeValue, String>(){
			@Override
			public String transform(AbstractAttributeValue arg0) {
				return arg0.getStringValue();
			}
		});
	}
	
	@Override
	@Transient
	public AbstractAttribute clone() throws CloneNotSupportedException {
		AbstractAttribute attribute = (AbstractAttribute) super.clone();
		attribute.setId(UUID.randomUUID());
		attribute.setName(new String(getName()));
		attribute.setCreatedBy(null);
		attribute.setCreatedDate(null);
		attribute.setUpdatedBy(null);
		attribute.setUpdatedDate(null);
		attribute.setCategory(new HashSet<Category>());
		attribute.setAttributeValues(new ArrayList<AbstractAttributeValue>());
		return attribute;
	}	
	
	@Override
	@Transient
	public boolean canRemove() {
		return (attributeValues.isEmpty() && category.isEmpty());
	}
	
}
