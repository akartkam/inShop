package com.akartkam.inShop.domain.order;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.validator.HtmlSafe;

@Entity
@Table(name = "Store")
public class Store extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8766618611021715980L;
	
	private String name;
	private String longDescription;
	private String address;
	private String phone;
	private String imageUrl;
	private String workSchedule;
	private String mapScript;
	private Set<Category> excludedCategories;

	@NotEmpty
	@Column(name = "name")	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	@Column(name = "address")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "phone")
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Column(name = "image_url")
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	@Column(name = "work_schedule")
	public String getWorkSchedule() {
		return workSchedule;
	}
	public void setWorkSchedule(String workSchedule) {
		this.workSchedule = workSchedule;
	}
	
	@Column(name = "map_script")
	public String getMapScript() {
		return mapScript;
	}
	public void setMapScript(String mapScript) {
		this.mapScript = mapScript;
	}
	
	@OneToMany
	@JoinTable(
			name = "store_excluded_category",
			joinColumns = {@JoinColumn(name = "stock_id")},
			inverseJoinColumns = {@JoinColumn(name = "category_id")}
			)
	public Set<Category> getExcludedCategories() {
		return excludedCategories;
	}
	public void setExcludedCategories(Set<Category> excludedCategories) {
		this.excludedCategories = excludedCategories;
	}
	
	
}
