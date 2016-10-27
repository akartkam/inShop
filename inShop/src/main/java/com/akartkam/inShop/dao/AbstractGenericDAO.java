package com.akartkam.inShop.dao;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.DomainObject;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;


@SuppressWarnings("unchecked")
public abstract class AbstractGenericDAO<T extends DomainObject<UUID>> implements GenericDAO<T, UUID> {

    private Class<T> domainClass;

    @Autowired
	private SessionFactory sessionFactory;
    


    public AbstractGenericDAO() {
        this.domainClass = (Class<T>) ((ParameterizedType) getClass()
                                .getGenericSuperclass()).getActualTypeArguments()[0];
      }
	
     private Class<T> getDomainClass() {
		return domainClass;
	}
    
     protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
	@Override
	public T findById(UUID id, boolean lock) {
        T entity;
        if (lock)
            entity = (T) currentSession().load(getDomainClass(), id, LockOptions.UPGRADE);
        else
            entity = (T) currentSession().load(getDomainClass(), id);

        return entity;
	}
	
	@Override
	public T findByUrl(String url){
		Method methodGetUrl = null;
		try {
			methodGetUrl = domainClass.getMethod("getUrl", null);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		if (methodGetUrl != null) {
			Criteria criteria = currentSession().createCriteria(domainClass);
			criteria.add(Restrictions.eq("url", url));
			List<T> res = criteria.list();
 			return !res.isEmpty()? res.get(0): null;
		}
		return null;
	}

	@Override
	public T create(T object) {
		currentSession().save(object);
        return object;
	}

	@Override
	public void delete(T object) {
		currentSession().delete(object);
	}


	@Override
	public T get(UUID id) {
		return (T) currentSession().get(getDomainClass(), id);
	}


	@Override
	public void update(T object) {
		currentSession().saveOrUpdate(object);
	}


	@Override
	public void deleteById(UUID id) {
		delete(findById(id, false));
	}
	
	@Override
	public void refresh(T object){
		currentSession().refresh(object);
	}
	
	@Override
	public List<T> list(){
		Criteria criteria = currentSession().createCriteria(domainClass);
		if (AbstractDomainObjectOrdering.class.isAssignableFrom(domainClass)) 
			criteria.addOrder(Order.asc("ordering"));
		return criteria.list();
	}
	
}
