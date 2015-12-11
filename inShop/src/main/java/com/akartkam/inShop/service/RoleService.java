package com.akartkam.inShop.service;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.RoleType;

public interface RoleService {
	boolean createRole(Role role);
	void updateRole(Role role);
	Role getRoleByRoletype(RoleType role);
	Role getRoleByName(String name);
	List<Role> getAllRoles(); 
	Role getRoleById(UUID id);
	
}
