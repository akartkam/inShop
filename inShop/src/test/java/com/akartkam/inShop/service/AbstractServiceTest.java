package com.akartkam.inShop.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.RoleType;
import com.akartkam.inShop.domain.UserDetailsAdapter;

@ContextConfiguration("classpath:ServiceTest-context.xml")
public abstract class AbstractServiceTest extends AbstractTransactionalJUnit4SpringContextTests {


	@BeforeTransaction 
	public void setUp() throws Exception {
		Account account = new Account();
    	account.setId(UUID.fromString("0b90130d-2c19-4001-b61a-ef100d7b950e"));
    	account.setFirstName("Artur");
    	account.setLastName("Akchurin");
    	account.setMiddleName("Kamilevich");
    	account.setUsername("akartkam");
    	account.setEnabled(true);
    	Role role = new Role();
    	role.setId(UUID.fromString("53a5c8ce-4f56-4c79-acb3-e47d76e580df"));
    	role.setRole(RoleType.ADMIN);
    	role.setName("Admin");
    	role.setEnabled(true);
    	Set<Role> roles = new HashSet<Role>(); 
    	roles.add(role);
    	account.setRoles(roles);
		SecurityContext context = new SecurityContextImpl();
		UserDetailsAdapter userDetailsAdapter = new UserDetailsAdapter(account);
		userDetailsAdapter.setPassword("shvabroid1");
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				userDetailsAdapter, userDetailsAdapter.getPassword(), userDetailsAdapter.getAuthorities());
		token.setDetails(userDetailsAdapter);
		context.setAuthentication(token);
		SecurityContextHolder.setContext(context);
	}
	
	@AfterTransaction
	public void endUp() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
	
}
