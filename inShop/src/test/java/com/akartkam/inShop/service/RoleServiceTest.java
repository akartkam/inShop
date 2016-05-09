package com.akartkam.inShop.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.hsqldb.util.DatabaseManagerSwing;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.junit.Assume;

import com.akartkam.inShop.common.AbstractTest;
import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.RoleType;

public class RoleServiceTest extends AbstractTest {

	private static final String SELECT_ROLE_NAME_QUERY =
			"select name from role where id = '53a5c8ce-4f56-4c79-acb3-e47d76e580df'";

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	protected RoleService roleService;
	@Autowired
	protected AccountService accountService;
	
	
	@Test
	public void FindById_AccountServiceTest(){
		//For conditonaly running test
		//Assume.assumeTrue(false);
		Account account = accountService.getAccountByUsername("akartkam");
		assertNotNull(account);
		assertEquals("akartkam", account.getUsername());
	}
	
	@Test
	public void insert_updateTestRoleServiceTest(){
	  Role role = new Role();
	  role.setName("Test");
	  boolean isCreated = createTestRole(role);
	  //Need to generate CreatedBy and CreatedDate
	  sessionFactory.getCurrentSession().flush();
	  assertTrue(isCreated);
	  assertNotNull(role.getCreatedBy());
	  assertNotNull(role.getCreatedDate());
	  role = roleService.getRoleById(UUID.fromString("53a5c8ce-4f56-4c79-acb3-e47d76e580df"));
	  role.setName("Test1");
	  roleService.updateRole(role);
	  assertEquals("Test1", role.getName());
  	  // Show that we haven't flushed the update to the database yet
	  String Name = jdbcTemplate.queryForObject(SELECT_ROLE_NAME_QUERY, String.class);
	  assertEquals("Admin", Name);	
	  // Manual flush required to avoid false positives in test
	  sessionFactory.getCurrentSession().flush();	
	  // Show that the flush worked, and now the update is in the database
	  String updatedName = jdbcTemplate.queryForObject(SELECT_ROLE_NAME_QUERY, String.class);
	  assertEquals("Test1", updatedName);	  
	}
	
	//@After 
	public void startDBManager() throws IOException {
		//hsqldb
		//DatabaseManagerSwing.main(new String[] { "--url", "jdbc:hsqldb:mem:dataSource", "--user", "sa", "--password", "" });
		//System.in.read();
	}
	

	private boolean createTestRole(Role role) {
		  role.setRole(RoleType.TEST);
		  role.setEnabled(false);
		  return roleService.createRole(role);
	}
	

}
