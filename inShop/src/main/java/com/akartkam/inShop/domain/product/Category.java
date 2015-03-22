package com.akartkam.inShop.domain.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;

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
//@SQLDelete(sql="UPDATE category SET enabled = FALSE WHERE id = ? AND version = ?")
@Proxy(lazy=false)
public class Category extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7426672791950430130L;
	
	private String name;
	private Category parent;
	private List<Category> subCategory;
	private List<Product> products;
	private String description;
	private String longDescription; 
	private List<AbstractAttribute> attributes; 
	


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
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	
	@OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
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
		this.subCategory.add(subCategory);
	}
	
	public void removeSubCategory (Category subCategory) {
		if (subCategory == null) throw new IllegalArgumentException("Null child category!");
		subCategory.setParent(null);
		this.subCategory.remove(subCategory);
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
	
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "long_description", length = Integer.MAX_VALUE - 1)
	public String getLongDescription() {
		return longDescription;
	}
    
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public List<AbstractAttribute> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(List<AbstractAttribute> attributes) {
		this.attributes = attributes;
	}	
	
	public void addAttribute (AbstractAttribute attribute) {
		if (attribute == null) throw new IllegalArgumentException("Null attribute!");
		attributes.add(attribute);
		attribute.setCategory(this);
	}
	
	@Transient
	public List<Category> buildCategoryHierarchy(List<Category> currentHierarchy) {
        if (currentHierarchy == null) {
            currentHierarchy = new ArrayList<Category>();
            currentHierarchy.add(this);
        }
        if (getParent() != null && ! currentHierarchy.contains(getParent())) {
            currentHierarchy.add(getParent());
            getParent().buildCategoryHierarchy(currentHierarchy);
        }
        return currentHierarchy;		
	}
	
	@Transient
	public List<Category> buildSubCategoryHierarchy(List<Category> currentHierarchy) {
        if (currentHierarchy == null) {
            currentHierarchy = new ArrayList<Category>();
        }
        if (!currentHierarchy.contains(this)) currentHierarchy.add(this);
        if (hasSubCategory()) {
            for(Category sc: getSubCategory()){
            	if (!currentHierarchy.contains(sc)) {
            		currentHierarchy.add(sc);	
            		sc.buildSubCategoryHierarchy(currentHierarchy);
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
            currentHierarchy.addAll(getAttributes());
        }		
		for(Category category : getSubCategory()) {
			if (category.isEnabled()) {
				currentHierarchy.addAll(category.getAttributes());
				category.getAllAttributes(currentHierarchy);
			}
		}
		return currentHierarchy;
	}
	
}
