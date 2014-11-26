package com.akartkam.inShop.service;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;





import com.akartkam.inShop.dao.RoleDAO;
import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.Roletype;

@ContextConfiguration
public class RoleServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired 
	private RoleDAO roleDao;
	
	@Autowired
	protected RoleService roleService;

	@Test
	public void sampleRoleServiceTest(){
	  int countRoleBefore = countRowsInTable("Role");
	  Role role = new Role();
	  UUID id = role.getId(); 
	  role.setName("Admin");
	  role.setRole(Roletype.ADMIN);
	  boolean isCreated = roleService.createRole(role);
	  int countRoleAfter = countRowsInTable("Role");
	  assertTrue(isCreated);
	  assertEquals(countRoleAfter, countRoleBefore+1);
	  Role roleReaded = roleService.getRoleByName("Admin", null);
	  assertEquals(id, roleReaded.getId());
	}
	

}
