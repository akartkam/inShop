package com.akartkam.inShop.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;


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
	protected String urlForForm;

    
    @Transient    
	public abstract String getName();

	@Transient
	public abstract String getUrl() ;
	
	public abstract void setUrl(String url);
	
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
    
	protected void buildShortUrl() {
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
