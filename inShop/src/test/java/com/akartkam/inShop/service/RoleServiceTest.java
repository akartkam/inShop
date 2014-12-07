package com.akartkam.inShop.service;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.Roletype;

@TransactionConfiguration(defaultRollback=false)
public class RoleServiceTest extends AbstractServiceTest {


	@Autowired
	protected RoleService roleService;
	
	
	@Test
	public void insert_updateTestRoleServiceTest(){
	  jdbcTemplate.execute("delete from Role where name like 'Test%'");
	  Role role = new Role();
	  boolean isCreated = createTestRole(role);
	  assertTrue(isCreated);
	  role.setName("Test1");
	}
	
	@After
	@Test
	public void checkAuditTest() {
	  Role role = roleService.getRoleByName("Test1");	
	  assertNotNull(role);
	  assertNotNull(role.getCreatedBy());
	  assertNotNull(role.getCreatedDate());
	  assertNotNull(role.getUpdatedBy());
	  assertNotNull(role.getUpdatedDate());
	  System.out.println("Create date: " + role.getCreatedDate());
	}
	

	private boolean createTestRole(Role role) {
		  role.setName("Test");
		  role.setRole(Roletype.TEST);
		  role.setEnabled(false);
		  return roleService.createRole(role);
	}
	

}
