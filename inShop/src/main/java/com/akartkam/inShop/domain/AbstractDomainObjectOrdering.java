package com.akartkam.inShop.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.DecimalMin;

import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;


@MappedSuperclass
public abstract class AbstractDomainObjectOrdering  extends AbstractDomainObject 
                                                    implements Comparable<AbstractDomainObjectOrdering>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4923961082547539283L;
	private Integer ordering;

	@AdminPresentation(tab=EditTab.ADDITIONAL)
	@DecimalMin("0")
	@Column(name="Ordering")
	public Integer getOrdering() {
		return ordering;
	}

	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}

	@Override
	public int compareTo(AbstractDomainObjectOrdering obj) {
		return Integer.valueOf(ordering).compareTo(Integer.valueOf(obj.ordering)); 
	}
	
	
	
}
