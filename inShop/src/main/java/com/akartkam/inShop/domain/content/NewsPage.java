package com.akartkam.inShop.domain.content;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;

@Entity
@Table(name = "News_page")
public class NewsPage extends AbstractContent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5930440716105060453L;
	private DateTime submitDate;
	private String description;
	
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description", length = Integer.MAX_VALUE - 1)
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
    @DateTimeFormat(pattern="${date.formatshort}")
    @Column(name = "submit_date")
    @Past	
	public DateTime getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(DateTime submitDate) {
		this.submitDate = submitDate;
	}

	@Override
	@Transient
	public NewsPage clone() throws CloneNotSupportedException {
		NewsPage page = (NewsPage) super.clone();
		return page;
	}
	
	@Override
	public int compareTo(AbstractDomainObjectOrdering obj) {
		if (obj instanceof NewsPage) {
			NewsPage npObj =  (NewsPage) obj;
			return npObj.getSubmitDate().compareTo(this.getSubmitDate());
		} else {
			return super.compareTo(obj); 
		}
	}	
	
}
