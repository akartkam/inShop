/*
 * В основном класс служит для тестирования
 * 
 */
package com.akartkam.inShop.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.Roletype;

@Repository
public class RoleDAOImpl extends AbstractGenericDAO<Role> implements RoleDAO {

	@Override
	public Role findRoleByName(String name, Roletype role) {
		Query q = currentSession().getNamedQuery("findRoleByName");
		q.setParameter("name", name);
		q.setParameter("role", role);
		return (Role) q.uniqueResult();
	}

}
