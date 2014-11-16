package com.akartkam.inShop.dao;

import java.io.Serializable;


import com.akartkam.inShop.domain.DomainObject;

public interface GenericDao<T extends DomainObject<ID>, ID extends Serializable> {
	public T findById(ID id, boolean lock);
	public T makePersistent(T object);
	public void makeTransient(T object);
}
