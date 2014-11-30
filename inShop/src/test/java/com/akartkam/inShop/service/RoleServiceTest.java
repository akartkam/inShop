package com.akartkam.inShop.service;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.Roletype;

@TransactionConfiguration(defaultRollback=false)
public class RoleServiceTest extends AbstractServiceTest {


	@Autowired
	protected RoleService roleService;
	

	
	@Test
	public void addTestRoleServiceTest(){
	  Role role = new Role();
	  UUID id = role.getId(); 
	  role.setName("Test");
	  role.setRole(Roletype.TEST);
	  boolean isCreated = roleService.createRole(role);
	  assertTrue(isCreated);
	  Role roleReaded = roleService.getRoleByName("Test");
	  assertEquals(id, roleReaded.getId());
	  
	}
	

}
