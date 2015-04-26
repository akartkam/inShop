package com.akartkam.inShop.domain.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.domain.AbstractDomainObject;

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
    @Index(name="category_url_index", columnNames={"url"})	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}	

}
