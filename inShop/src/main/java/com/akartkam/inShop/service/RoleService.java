package com.akartkam.inShop.service;

import com.akartkam.inShop.domain.Role;

public interface RoleService {
	boolean createRole(Role role);
	Role getRoleByName(String name, String role);
}
