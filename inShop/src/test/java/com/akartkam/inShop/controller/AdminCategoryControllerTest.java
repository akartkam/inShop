package com.akartkam.inShop.controller;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.akartkam.inShop.common.AbstractTest;
import com.akartkam.inShop.controller.admin.product.AdminCategoryController;
import com.akartkam.inShop.dao.product.CategoryDAO;
import com.akartkam.inShop.domain.product.Category;

public class AdminCategoryControllerTest extends AbstractTest {
	@Inject private AdminCategoryController adminCategoryController;
	@Inject private CategoryDAO categoryDao;
	@Inject private SessionFactory sessionFactory;
	
	private SessionFactory badSessionFactory;
	private Model model;
	
	@Before
	public void setUp() throws Exception {
		this.badSessionFactory = mock(SessionFactory.class);
		when(badSessionFactory.getCurrentSession())
			.thenThrow(new HibernateException("Problem getting current session"));
		this.model = new ExtendedModelMap();		
	}
	
	@After
	public void tearDown() throws Exception {
		this.badSessionFactory = null;
		this.jdbcTemplate = null;
	}	
	
	@Test(expected = HibernateException.class)
	@DirtiesContext
	public void testGetContactWithBadSessionFactory() {
		ReflectionTestUtils.setField(categoryDao, "sessionFactory", badSessionFactory);
		List<Category> c = categoryDao.findCategoryByName("qwer");
	}	
}
