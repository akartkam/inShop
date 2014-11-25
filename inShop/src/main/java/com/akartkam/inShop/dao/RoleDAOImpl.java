/*
 * В основном класс служит для тестирования
 * 
 */
package com.akartkam.inShop.dao;

import org.hibernate.Query;

import com.akartkam.inShop.domain.Role;

public class RoleDAOImpl extends AbstractGenericDAO<Role> implements RoleDAO {

	@Override
	public Role findRoleByName(String name, String role) {
		Query q = currentSession().getNamedQuery("findRoleByName");
		q.setParameter("name", name);
		q.setParameter("role", role);
		return (Role) q.uniqueResult();
	}

}
