package com.akartkam.inShop.domain.product;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;

@NamedQueries({
@NamedQuery(
		name = "readAllCategories",
		query = "FROM Category ORDER BY ordering")//,
//@NamedQuery(
		//name = "readAllSubCategories",
		//query = "FROM Category ct
        //       LEFT JOIN ct.allParentCategoryXrefs xref
        //       WHERE category.defaultParentCategory.id IN :parentCategoryId OR xref.category.id IN :parentCategoryId
        //       ORDER BY xref.displayOrder, category.id")
})
@Entity
@Table(name = "Category")
public class Category extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7426672791950430130L;
	
	private String name;
	private Category parent;
	private List<Product> products;
	private String description;
	private String longDescription; 
	

	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "name", unique=true)
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
	
	@OneToMany(mappedBy = "category")
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
    @Lob
    @Type(type = "org.hibernate.type.MaterializedClobType")
    @Column(name = "long_description", length = Integer.MAX_VALUE - 1)
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}	
	
	

}
