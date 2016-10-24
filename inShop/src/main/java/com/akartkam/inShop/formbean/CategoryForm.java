package com.akartkam.inShop.formbean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;

public class CategoryForm extends Category {

	/**
	 * 
	 */
	private static final long serialVersionUID = -500394871795849096L;

	private List<AbstractAttribute> attributesForForm = new ArrayList<AbstractAttribute>();
	private String urlForForm;
	
	public CategoryForm() { }
	public CategoryForm(Category category) { 
		this.setId(category.getId());
		this.setCreatedDate(category.getCreatedDate());
		this.setAttributes(new HashSet<AbstractAttribute>(category.getAttributes()));
	}
	
	/*if (url != null && !"".equals(url)) {
		String[] splitedUrl = url.split("/"); 
		urlForForm = splitedUrl[splitedUrl.length-1];		
	}*/

	
	//this.attributesForForm = new ArrayList<AbstractAttribute>(attributes);
	
	//for form

	public List<AbstractAttribute> getAttributesForForm() {
		return attributesForForm;
	}
	
	@NotEmpty
	@Pattern(regexp="^[a-z0-9-]*$", message="{error.bad.urlForForm}")
	public String getUrlForForm() {
		return urlForForm;
	}	
	
	public void setAttributesForForm(List<AbstractAttribute> attributesForForm) {
		this.attributesForForm = attributesForForm;
	}
	public void setUrlForForm(String urlForForm) {
		this.urlForForm = urlForForm;
	}	
	
	
}
