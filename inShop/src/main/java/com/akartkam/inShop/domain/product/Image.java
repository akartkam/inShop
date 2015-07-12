package com.akartkam.inShop.domain.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
/*Пока не используется*/
@Embeddable
public class Image implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4864282596759515293L;
	private String imageUrl;
	private int ordering;
	
	@Column(name="image_url")
	public String getImageUrl() {
		return imageUrl;
	}
	
	@Column(name="ordering")
	public int getOrdering() {
		return ordering;
	}
	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	public void setImageUrl(String url) {
		this.imageUrl = url;
	}
	
}
