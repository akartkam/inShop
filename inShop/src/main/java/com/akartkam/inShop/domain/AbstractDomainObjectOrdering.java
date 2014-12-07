package com.akartkam.inShop.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractDomainObjectOrdering  extends AbstractDomainObject 
                                                    implements Comparable<AbstractDomainObjectOrdering>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4923961082547539283L;
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
