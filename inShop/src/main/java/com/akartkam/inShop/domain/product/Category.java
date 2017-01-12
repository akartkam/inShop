package com.akartkam.inShop.domain.product;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.AbstractWebDomainObject;
import com.akartkam.inShop.domain.Instruction;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.attribute.AttributeValuesHolderType;
import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;
import com.akartkam.inShop.validator.HtmlSafe;

@NamedQueries({
@NamedQuery(
		name = "readAllCategories",
		query = "FROM Category WHERE enabled = true ORDER BY ordering"),
@NamedQuery(
		name = "readRootCategories",
		query = "FROM Category ct WHERE ct.parent IS NULL AND enabled = true ORDER BY ct.ordering"),

@NamedQuery(
		name = "findCategoryByName",
		query = "FROM Category ct WHERE ct.name = :name AND enabled = true ORDER BY ct.ordering")

})
@Entity
@Table(name = "Category")
public class Category extends AbstractWebDomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7426672791950430130L;
	
	private String name;
	private Category parent;
	private List<Category> subCategory = new ArrayList<Category>();
	private List<Product> products = new ArrayList<Product>();
	private String description;
	private String longDescription; 
	private Set<AbstractAttribute> attributes = new HashSet<AbstractAttribute>(0);
	private String url;
	private Boolean showQuanPerPackOnProductHeader;
	private Instruction instruction;
	
	@AdminPresentation(tab=EditTab.MAIN)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne
	@JoinColumn
	@ForeignKey(name="fk_parent")
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	
    @Column(name = "url")
    @Index(name="category_url_index", columnNames={"url"})	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
		
	@OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			  org.hibernate.annotations.CascadeType.DELETE})
	@OrderBy("ordering")
	@BatchSize(size = 10)
	public List<Category> getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(List<Category> subCategory) {
		this.subCategory = subCategory;
	}
	
	public void addSubCategory (Category subCategory) {
		if (subCategory == null) throw new IllegalArgumentException("Null child category!");
		if (subCategory.getParent() != null) 
			subCategory.getParent().removeSubCategory(subCategory);
		subCategory.setParent(this);
		this.getSubCategory().add(subCategory);
	}
	
	public void removeSubCategory (Category subCategory) {
		if (subCategory == null) throw new IllegalArgumentException("Null child category!");
		subCategory.setParent(null);
		this.getSubCategory().remove(subCategory);
	}
	
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public void addProduct (Product product) {
		if (product == null) throw new IllegalArgumentException("Null product!");
		products.add(product);
		product.setCategory(this);
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@HtmlSafe
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "long_description", length = Integer.MAX_VALUE - 1)
	public String getLongDescription() {
		return longDescription;
	}
    
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
	@Cascade({org.hibernate.annotations.CascadeType.ALL})
	@JoinTable(name = "lnk_category_attribute", joinColumns = { 
			@JoinColumn(name = "CATEGORY_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "ATTRIBUTE_ID", 
					nullable = false, updatable = false) })
	public Set<AbstractAttribute> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(Set<AbstractAttribute> attributes) {
		this.attributes = attributes;
	}	
	
	public void addAttribute (AbstractAttribute attribute) {
		if (attribute == null) throw new IllegalArgumentException("Null attribute!");
		attributes.add(attribute);
	}
	
	public void removeAttribute (AbstractAttribute attribute) {
		if (attribute == null) throw new IllegalArgumentException("Null attribute!");
		attributes.remove(attribute);
		attribute.getCategory().remove(this);
	}
	
	public void buildFullLink(String shortUrl) {
		if (shortUrl == null || "".equals(shortUrl)) return;
		if (shortUrl.startsWith("/")) shortUrl = shortUrl.substring(1);     
        StringBuilder linkBuffer = new StringBuilder(50);
        if (this.hasParentCategory()) linkBuffer.append(this.getParent().getUrl()).append("/").append(shortUrl);
        else linkBuffer.append("/").append(shortUrl);
        setUrl(linkBuffer.toString());		
	}
	
	@Column(name = "show_quant_per_pack_on_prod_header")
	public Boolean getShowQuanPerPackOnProductHeader() {
		return showQuanPerPackOnProductHeader == null? false: showQuanPerPackOnProductHeader;
	}
	public void setShowQuanPerPackOnProductHeader(
			Boolean isUseQuanPerPackOnProductHeader) {
		this.showQuanPerPackOnProductHeader = isUseQuanPerPackOnProductHeader;
	}
	
	@ManyToOne
	@JoinColumn
	@ForeignKey(name="fk_category_instruction")
	public Instruction getInstruction() {
		return instruction;
	}
	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}	
	
	
	@Transient
	public List<Category> buildCategoryHierarchy(List<Category> currentHierarchy, Boolean reverse) {
        if (currentHierarchy == null) {
            currentHierarchy = new ArrayList<Category>();
            currentHierarchy.add(this);
        }
        if (getParent() != null && ! currentHierarchy.contains(getParent())) {
            currentHierarchy.add(getParent());
            getParent().buildCategoryHierarchy(currentHierarchy, reverse);
        }
        if(reverse && getParent()== null) Collections.reverse(currentHierarchy);
        return currentHierarchy;		
	}
	
	@Transient
	public List<Category> buildSubCategoryHierarchy(List<Category> currentHierarchy, Boolean useDisabled) {
        if (currentHierarchy == null) {
            currentHierarchy = new ArrayList<Category>();
        }
        if (!useDisabled && !this.isEnabled()) {
        	return currentHierarchy;
        }
        if (!currentHierarchy.contains(this)) currentHierarchy.add(this);
        if (hasSubCategory()) {
            for(Category sc: getSubCategory()){
            	if (!currentHierarchy.contains(sc)) {
            		//currentHierarchy.add(sc);
            		sc.buildSubCategoryHierarchy(currentHierarchy, useDisabled);
            	}
            }
        }
        return currentHierarchy;		
	}
	
	
	@Transient
	public boolean hasSubCategory() {
		return !subCategory.isEmpty(); 
	}
	
	@Transient
	public boolean hasParentCategory() {
		return parent != null; 
	}

	@Transient
	public List<Product> getAllProducts(List<Product> currentHierarchy) {
        if (currentHierarchy == null) {
            currentHierarchy = new ArrayList<Product>();
            currentHierarchy.addAll(getProducts());
        }		
		for(Category category : getSubCategory()) {
			if (category.isEnabled()) {
				currentHierarchy.addAll(category.getProducts());
				category.getAllProducts(currentHierarchy);
			}
		}
		return currentHierarchy;
	}
	
	@Transient
	public List<AbstractAttribute> getAllAttributes (List<AbstractAttribute> currentHierarchy) {
        if (currentHierarchy == null) {
            currentHierarchy = new ArrayList<AbstractAttribute>();
        }	
        if (currentHierarchy.isEmpty())        
	        for (AbstractAttribute at: getAttributes()) {
	        	if (!currentHierarchy.contains(at) && at.isEnabled()) currentHierarchy.add(at);        
	        }
        for(Category category : getSubCategory()) {
			if (category.isEnabled()) {
				for (AbstractAttribute at: category.getAttributes()) if (at.isEnabled()) currentHierarchy.add(at);
				category.getAllAttributes(currentHierarchy);
			}
		}
		return currentHierarchy;
	}
	
	@Transient
	public List<AbstractAttribute> getAllAttributes (List<AbstractAttribute> currentHierarchy, AttributeValuesHolderType valueHolde) {
        if (currentHierarchy == null) {
            currentHierarchy = new ArrayList<AbstractAttribute>();
        }	
        if (currentHierarchy.isEmpty())        
	        for (AbstractAttribute at: getAttributes()) {
	        	if (!currentHierarchy.contains(at) && at.isEnabled() && at.getAttributeValuesHolder().equals(valueHolde)) currentHierarchy.add(at);        
	        }
        for(Category category : getSubCategory()) {
			if (category.isEnabled()) {
				for (AbstractAttribute at: category.getAttributes()) if (at.isEnabled() && at.getAttributeValuesHolder().equals(valueHolde)) currentHierarchy.add(at);
				category.getAllAttributes(currentHierarchy, valueHolde);
			}
		}
		return currentHierarchy;
	}
	
	@Transient
	public List<AbstractAttribute> getAllAttributes (List<AbstractAttribute> currentHierarchy, boolean up) {
        if (currentHierarchy == null) {
            currentHierarchy = new ArrayList<AbstractAttribute>();
        }
        if (currentHierarchy.isEmpty())
	        for (AbstractAttribute at: getAttributes()) {
	        	if (!currentHierarchy.contains(at) && at.isEnabled()) currentHierarchy.add(at);        
	        }
        if (!up) return getAllAttributes(currentHierarchy);
        Category parent = getParent();
        if (parent!=null) {
        	for (AbstractAttribute at: parent.getAttributes()) if (at.isEnabled()) currentHierarchy.add(at);
        	parent.getAllAttributes(currentHierarchy, up);
        }
        return currentHierarchy;
	}
	
	@Transient
	public List<AbstractAttribute> getAllAttributes (List<AbstractAttribute> currentHierarchy, boolean up, AttributeValuesHolderType valueHolde) {
        if (currentHierarchy == null) {
            currentHierarchy = new ArrayList<AbstractAttribute>();
        }
        if (currentHierarchy.isEmpty())
	        for (AbstractAttribute at: getAttributes()) {
	        	if (!currentHierarchy.contains(at) && at.isEnabled() && at.getAttributeValuesHolder().equals(valueHolde)) currentHierarchy.add(at);        
	        }
        if (!up) return getAllAttributes(currentHierarchy);
        Category parent = getParent();
        if (parent!=null) {
        	for (AbstractAttribute at: parent.getAttributes()) if (at.isEnabled()) currentHierarchy.add(at);
        	parent.getAllAttributes(currentHierarchy, up, valueHolde);
        }
        return currentHierarchy;
	}

	
	
	@Transient
	public List<AbstractAttribute> getAllAttributes (boolean up) {
		List<AbstractAttribute> currentHierarchy = new ArrayList<AbstractAttribute>();
		return getAllAttributes(currentHierarchy, up);
	}

	
	@SuppressWarnings("rawtypes")
	@Transient
	public List<AbstractAttributeValue> getAllAttributeValues (boolean up) {
		List<AbstractAttribute> la = new ArrayList<AbstractAttribute>(); 
		la = getAllAttributes(la,up);
        List <AbstractAttributeValue> currentHierarchy = new ArrayList<AbstractAttributeValue>();
        for (AbstractAttribute at: la)
        	for (AbstractAttributeValue av: at.getAttributeValues())
        		if (av.isEnabled()) currentHierarchy.add(av);        
        return currentHierarchy;	
	}
	
	
	@Override
	@Transient
	public Category clone() throws CloneNotSupportedException {
		Category category = (Category) super.clone();
		category.setId(UUID.randomUUID());
		category.setName(new String(getName()));
		category.setCreatedBy(null);
		category.setCreatedDate(null);
		category.setUpdatedBy(null);
		category.setUpdatedDate(null);
		category.setAttributes(new HashSet<AbstractAttribute>());
		category.setDescription(getDescription() != null? new String(getDescription()): null);
		category.setLongDescription(getLongDescription() != null? new String(getLongDescription()): null);
		category.setProducts(new ArrayList<Product>());
		category.setSubCategory(new ArrayList<Category>());
		return category;
	}
	
	
	@Transient
	public boolean hasAttributes() {
		return !getAttributes().isEmpty();
	}
	
	@Transient
	@Override
	public boolean canRemove() {
		return (!hasSubCategory() && !hasAttributes() && products.isEmpty());
	}
	
	@Transient
	public int getDepthNesting() {
		int depth = 0;
		Category cat = this;
		while (cat.hasParentCategory()) {
			depth+=1;
			cat = cat.getParent();
		}
		return depth;	
	}
	
	@Transient
	public String buildFullName() {
		StringBuilder res= new StringBuilder(this.getName());
		Category cat = this;
		while (cat.hasParentCategory()) {
			res.insert(0, "/");
			res =  res.insert(0, cat.getParent().getName());
			cat = cat.getParent();
		}
		return res.toString();		
	}
	
}
