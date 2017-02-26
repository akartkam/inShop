package com.akartkam.inShop.domain.product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.domain.AbstractWebDomainObject;
import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;
import com.akartkam.inShop.validator.HtmlSafe;

@Entity
@Table(name = "Brand")
public class Brand extends AbstractWebDomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4686970176196323428L;
	private String name;
	private String description;
	private String longDescription;
	private String url;
	private String logoUrl;
	private List<Product> products = new ArrayList<Product>();
	private String urlForForm;
	
	@AdminPresentation(tab=EditTab.MAIN)
	@NotEmpty
	@Column(name = "name")
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
		brand.setLongDescription(new String(getLongDescription()));
		brand.setUrl(new String(getUrl()));
		brand.setLogoUrl(new String(getLogoUrl()));
		return brand;
	}
	
	@Override
	@Transient
	public boolean canRemove(){
		return getProducts().isEmpty();
	}
	
	@Transient
	@NotEmpty
	@Pattern(regexp="^[a-z0-9-]*$", message="{error.bad.urlForForm}")
	public String getUrlForForm() {
		if (urlForForm == null) {
			buildShortUrl();
		}
		return urlForForm;
	}
	public void setUrlForForm(String urlForForm) {
		this.urlForForm = urlForForm;
	}

	private void buildShortUrl() {
		if (getUrl() != null && !"".equals(getUrl())) {
			urlForForm = getUrl().replace("/", ""); 
		}	
	}
	
	public void buildFullLink(String shortUrl) {
		if (shortUrl == null || "".equals(shortUrl)) return;
		if (shortUrl.startsWith("/")) return;
        StringBuilder linkBuffer = new StringBuilder(50);
        linkBuffer.append("/").append(shortUrl);
        setUrl(linkBuffer.toString());		
	} 	


}
