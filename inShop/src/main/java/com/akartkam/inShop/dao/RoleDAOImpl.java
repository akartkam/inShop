/*
 * � �������� ����� ������ ��� ������������
 * 
 */
package com.akartkam.inShop.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.RoleType;

@Repository
public class RoleDAOImpl extends AbstractGenericDAO<Role> implements RoleDAO {

	@Override
	public Role findRoleByName(String name) {
		Query q = currentSession().getNamedQuery("findRoleByName");
		q.setParameter("name", name);
		return (Role) q.uniqueResult();
	}
	
	@Override
	public Role findRoleByRoletype(RoleType role) {
		Query q = currentSession().getNamedQuery("findRoleByRoletype");
		q.setParameter("role", role);
		return (Role) q.uniqueResult();
	}



}
