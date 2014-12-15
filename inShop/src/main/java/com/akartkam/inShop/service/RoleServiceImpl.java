package com.akartkam.inShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.RoleDAO;
import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.RoleType;

@Service("roleServiceImpl")
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {
	
	@Autowired 
	private RoleDAO roleDao;

	@Override
	@Transactional(readOnly = false)
	public boolean createRole(Role role) {
		boolean valid = validateRole(role);
		if (valid) {
			roleDao.create(role);
		}
		return valid;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void updateRole(Role role) {
		roleDao.update(role);
	}

	@Override
	public Role getRoleByName(String name) {
		return roleDao.findRoleByName(name);
	}
	
	@Override
	public Role getRoleByRoletype(RoleType role) {
		return roleDao.findRoleByRoletype(role);
	}	
	
	private boolean validateRole(Role role) {
		return (roleDao.findRoleByName(role.getName()) == null);
		
	}	

}
