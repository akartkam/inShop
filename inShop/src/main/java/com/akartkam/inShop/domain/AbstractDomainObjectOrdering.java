package com.akartkam.inShop.domain;

import javax.persistence.Column;

public abstract class AbstractDomainObjectOrdering  extends AbstractDomainObjectHibernateUUID  {
	private int ordering;

	@Column(name="Ordering")
	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
}
