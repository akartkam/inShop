package com.akartkam.inShop.domain.common.media;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.domain.AbstractDomainObject;

@Entity
@Table(name = "Media")
public class Media extends AbstractDomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1790326661772317354L;
	private String url;
	private String title;
    
	@NotNull
	@NotEmpty
	@Column(name = "url", nullable = false)
    @Index(name="media_url_index", columnNames={"url"})
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "title")
    @Index(name="media_url_title", columnNames={"title"})
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    

}
