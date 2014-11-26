package com.akartkam.inShop.service;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;


import org.springframework.test.context.transaction.TransactionConfiguration;

import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.Roletype;

@TransactionConfiguration(defaultRollback=false)
@ContextConfiguration
public class RoleServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {


	@Autowired
	protected RoleService roleService;

	@Autowired 
	MockAccountAuditorAwareImpl mockAccountAuditorAwareImpl;
	
	@Autowired 
	AuditingEntityListener<?> listener;	
	
	@Test
	public void sampleRoleServiceTest(){
	  //int countRoleBefore = countRowsInTable("Role");
	  Role role = new Role();
	  UUID id = role.getId(); 
	  role.setName("Admin");
	  role.setRole(Roletype.ADMIN);
	  boolean isCreated = roleService.createRole(role);
	  //int countRoleAfter = countRowsInTable("Role");
	  assertTrue(isCreated);
	  //assertSame(Integer.valueOf(countRoleBefore+1), Integer.valueOf(countRoleAfter));
	  Role roleReaded = roleService.getRoleByName("Admin", null);
	  assertEquals(id, roleReaded.getId());
	  
	}
	

}
