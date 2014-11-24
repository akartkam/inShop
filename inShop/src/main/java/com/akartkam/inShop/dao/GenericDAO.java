package com.akartkam.inShop.dao;

import java.io.Serializable;


import com.akartkam.inShop.domain.DomainObject;

public interface GenericDAO<T extends DomainObject<ID>, ID extends Serializable> {
	public T findById(ID id, boolean lock);
	public T get(ID id);
	public T create(T object);
	public void update(T object);
	public void deleteById(ID id);
	public void delete(T object);
}
