package com.akartkam.inShop.domain.product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.com.presentation.admin.AdminPresentation;
import com.akartkam.com.presentation.admin.EditTab;
import com.akartkam.inShop.domain.AbstractDomainObject;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;

@Entity
@Table(name = "Brand")
public class Brand extends AbstractDomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4686970176196323428L;
	private String name;
	private String description;
	private String url;
	private String logoUrl;
	private List<Product> products = new ArrayList<Product>();
	
	@AdminPresentation(tab=EditTab.MAIN)
	@NotNull
	@NotEmpty
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description", length = Integer.MAX_VALUE - 1)
	public String getDescription() {
		return description;
	}
    
	public void setDescription(String description) {
		this.description = description;
	}
	
    @Column(name = "url")
    @Index(name="brand_url_index", columnNames={"url"})	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
    @Column(name = "logo_url")
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	
	@OneToMany(mappedBy = "brand")
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
		
	@Override
	@Transient
	public Brand clone() throws CloneNotSupportedException {
		Brand brand = (Brand) super.clone();
		brand.setId(UUID.randomUUID());
		brand.setName(new String(getName()));
		brand.setCreatedBy(null);
		brand.setCreatedDate(null);
		brand.setUpdatedBy(null);
		brand.setUpdatedDate(null);
		brand.setDescription(new String(getDescription()));
		brand.setUrl(new String(getUrl()));
		brand.setLogoUrl(new String(getLogoUrl()));
		return brand;
	}
	
	@Override
	@Transient
	public boolean canRemove(){
		return getProducts().isEmpty();
	}

}
