package com.akartkam.inShop.dao;

import java.util.UUID;

import com.akartkam.inShop.domain.Role;

public interface RoleDAO extends GenericDAO<Role, UUID> {
	Role findRoleByName(String roleName);

}
