package com.akartkam.inShop.domain.product.attribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.Unit;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
						name = "DISCRIMINATOR",
						discriminatorType = DiscriminatorType.STRING
)
@Table(name = "Attribute")
@SuppressWarnings("rawtypes")
@Cache(region = "attr", usage = CacheConcurrencyStrategy.READ_WRITE)
@BatchSize(size = 50)
public abstract class AbstractAttribute extends AbstractDomainObjectOrdering {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1978623223082601133L;
	private String code;
	private String name;
	private String description;
	private AttributeCategory attributeCategory;
	private Set<Category> category = new HashSet<Category>(0);
	protected List<AbstractAttributeValue> attributeValues = new ArrayList<AbstractAttributeValue>(0);
	private AttributeValuesHolderType attributeValuesHolder;
	private Unit unit;
	private Boolean isShowOnProductHeader;
	
	
	@Size(max = 50)
	@Column(name = "code", unique=true)	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Size(min = 1, max = 50)
	@Column(name = "name", nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "description")
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "unit")
    @Enumerated(EnumType.STRING)
    public Unit getUnit() {
    	return unit;
    }
    
    public void setUnit(Unit unit) {
    	this.unit = unit;
    }
	
	@NotNull
	@Transient
	public abstract AttributeType getAttributeType();
	
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(mappedBy="attribute", cascade = CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	@BatchSize(size = 30)
	public List<AbstractAttributeValue> getAttributeValues() {
		return attributeValues;
	}	
	
	public void setAttributeValues(List<AbstractAttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
	}
	
	@AdminPresentation(tab=EditTab.ADDITIONAL)
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public AttributeCategory getAttributeCategory() {
		return attributeCategory;
	}
	public void setAttributeCategory(AttributeCategory attributeCategory) {
		this.attributeCategory = attributeCategory;
	}	

	@ManyToMany(mappedBy = "attributes")
	public Set<Category> getCategory() {
		return category;
	}
	
	public void setCategory(Set<Category> category) {
		this.category = category;
	}

    @NotNull
    @Column(name = "values_holder", nullable = false)
    //, columnDefinition="character varying(255) default 'PRODUCT'"
    @Enumerated(EnumType.STRING)	
	public AttributeValuesHolderType getAttributeValuesHolder() {
		return attributeValuesHolder;
	}
	public void setAttributeValuesHolder(
			AttributeValuesHolderType attributeValuesHolder) {
		this.attributeValuesHolder = attributeValuesHolder;
	}
	
	@Column(name = "show_on_prod_header")
	public Boolean getIsShowOnProductHeader() {
		return isShowOnProductHeader == null? false: isShowOnProductHeader;
	}
	public void setIsShowOnProductHeader(Boolean isShowOnProductHeader) {
		this.isShowOnProductHeader = isShowOnProductHeader;
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
		attribute.setUnit(getUnit());
		attribute.setAttributeValues(new ArrayList<AbstractAttributeValue>());
		return attribute;
	}	
	
	@Transient
	public boolean hasAttributeValues(){
		return !getAttributeValues().isEmpty();
	}
	
	@Override
	@Transient
	public boolean canRemove() {
		return (!hasAttributeValues() && category.isEmpty());
	}
	
	@Transient
	public String getDisplayName() {
		StringBuilder res = new StringBuilder();
		if (getCode() != null && !"".equals(getCode())){
			res.append("<span style='font-size:8pt'>").append(getCode()).append("</span> ").append(getName());
		} else {
			res.append(getName());
		}
		if (getDescription() != null && !"".equals(getDescription().trim())){
			res.append(" <span style='font-size:8pt'>(").append(getDescription()).append(")</span> ");
		}
		return res.toString();
	}
	
	public static class AVComparer implements Comparator<AbstractAttributeValue> {

		@Override
		public int compare(AbstractAttributeValue arg0, AbstractAttributeValue arg1) {
			AbstractAttribute at0 = (arg0!=null?arg0.getAttribute():null);
			AbstractAttribute at1 = (arg1!=null?arg1.getAttribute():null);
		    if (at0 == null ^ at1 == null) {
		        return (at0 == null) ? -1 : 1;
		    }

		    if (at0 == null && at1 == null) {
		        return 0;
		    }

			return at0.compareTo(at1) ;
		}
		  
	}

	
}
