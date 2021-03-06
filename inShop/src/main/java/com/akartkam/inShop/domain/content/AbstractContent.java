package com.akartkam.inShop.domain.content;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.domain.AbstractWebDomainObject;
import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;
import com.akartkam.inShop.validator.HtmlSafe;

@MappedSuperclass
public class AbstractContent extends AbstractWebDomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3111597288057717027L;
	private String name;
	private String url;
	private String longDescription;

	@AdminPresentation(tab=EditTab.MAIN)
	@NotEmpty
	@Column(name = "name")
	@Override
	public String getName() {
		return name;
	}
	
	

	public void setName(String name) {
		this.name = name;
	}

    @Column(name = "url")
    @Index(name="page_url_index", columnNames={"url"})	
	@Override
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
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
	
	@Override
	@Transient
	public AbstractContent clone() throws CloneNotSupportedException {
		AbstractContent page = (AbstractContent) super.clone();
		page.setId(UUID.randomUUID());
		page.setName(new String(getName()));
		page.setCreatedBy(null);
		page.setCreatedDate(null);
		page.setUpdatedBy(null);
		page.setUpdatedDate(null);
		page.setLongDescription(new String(getLongDescription()));
		page.setUrl(new String(getUrl()));
		return page;
	}	


}
