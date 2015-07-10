package com.akartkam.inShop.domain.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Image implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4864282596759515293L;
	private String image_url;
	private Boolean isDefault;
	
	@Column(name="image_url")
	public String getImageUrl() {
		return image_url;
	}
	public void setImageUrl(String url) {
		this.image_url = url;
	}
	
	@Column(name="is_default")
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
}
