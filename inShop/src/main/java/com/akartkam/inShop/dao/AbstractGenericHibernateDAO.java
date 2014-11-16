package com.akartkam.inShop.dao;

import com.akartkam.inShop.domain.DomainObject;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
public abstract class AbstractGenericHibernateDAO<T extends DomainObject<ID>, ID extends Serializable> implements GenericDao<T, ID> {

    private Class<T> persistentClass;


	private Session session;
    

    @Autowired
    public AbstractGenericHibernateDAO(Session session) {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.session = session;
     }
	
    
    public Class<T> getPersistentClass() {
		return persistentClass;
	}
    
    
    protected Session currentSession() {
        return session;
    }
    
	@Override
	public T findById(ID id, boolean lock) {
        T entity;
        if (lock)
            entity = (T) currentSession().load(getPersistentClass(), id, LockOptions.UPGRADE);
        else
            entity = (T) currentSession().load(getPersistentClass(), id);

        return entity;
	}

	@Override
	public T makePersistent(T object) {
		currentSession().saveOrUpdate(object);
        return object;
	}

	@Override
	public void makeTransient(T object) {
		currentSession().delete(object);
	}

}
