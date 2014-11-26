package com.akartkam.inShop.service;

import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.Roletype;

public interface RoleService {
	boolean createRole(Role role);
	Role getRoleByName(String name, Roletype role);
}
