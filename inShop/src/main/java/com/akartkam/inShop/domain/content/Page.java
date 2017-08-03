package com.akartkam.inShop.domain.content;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Page")
public class Page extends AbstractContent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1861911023550332591L;

	@Override
	@Transient
	public Page clone() throws CloneNotSupportedException {
		Page page = (Page) super.clone();
		return page;
	}

}
