package com.akartkam.inShop.dao;

import java.util.UUID;

import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.RoleType;

public interface RoleDAO extends GenericDAO<Role, UUID> {
	Role findRoleByName(String name);
	Role findRoleByRoletype(RoleType role);

}
