package com.akartkam.inShop.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;


@MappedSuperclass
public abstract class AbstractWebDomainObject extends AbstractDomainObjectOrdering  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7031912833021411146L;
    private String h1;
    private String metaDescription;
    private String metaTitle;
    private String metaKeywords;
    
    @Transient    
	public abstract String getName();

	@Transient
	public abstract String getUrl() ;
	
	@Column(name="h1")
	public String getH1() {
		return h1;
	}
	public void setH1(String h1) {
		this.h1 = h1;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	
	@Column(name="meta_title")
	public String getMetaTitle() {
		return metaTitle;
	}
	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}
	
	@Column(name="meta_description")
	public String getMetaKeywords() {
		return metaKeywords;
	}
	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

    
    
    
    
}
