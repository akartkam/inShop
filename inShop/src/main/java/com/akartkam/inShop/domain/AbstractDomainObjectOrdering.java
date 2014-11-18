package com.akartkam.inShop.domain;

import javax.persistence.Column;


public abstract class AbstractDomainObjectOrdering  extends AbstractDomainObjectHibernateUUID 
                                                    implements Comparable<AbstractDomainObjectOrdering>  {
	private int ordering;

	@Column(name="Ordering")
	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}

	@Override
	public int compareTo(AbstractDomainObjectOrdering obj) {
		return Integer.valueOf(ordering).compareTo(Integer.valueOf(obj.ordering)); 
	}
	
	
	
}
