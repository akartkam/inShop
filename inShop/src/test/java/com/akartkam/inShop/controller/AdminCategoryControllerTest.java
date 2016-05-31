package com.akartkam.inShop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;

import com.akartkam.inShop.common.AbstractTest;
import com.akartkam.inShop.controller.admin.product.AdminCategoryController;
import com.akartkam.inShop.dao.product.CategoryDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;
import com.akartkam.inShop.service.product.AttributeCategoryService;
import com.akartkam.inShop.service.product.CategoryService;

public class AdminCategoryControllerTest extends AbstractTest {

	@Autowired
	private CategoryDAO categoryDao;
	
	@Mock
	private SessionFactory badSessionFactory;	
	@Mock
	private CategoryService categoryService;
	@Mock
	private AttributeCategoryService attributeCategoryService;
	
	@InjectMocks
	private AdminCategoryController adminCategoryController;
	private MockMvc mockMvc;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(adminCategoryController).build();
		when(badSessionFactory.getCurrentSession())
			.thenThrow(new HibernateException("Problem getting current session"));
		when(categoryService.getAllCategoryHierarchy(any(Boolean.class))).thenReturn(new ArrayList<Category>());
		when(attributeCategoryService.getAllAttributeCategory()).thenReturn(new ArrayList<AttributeCategory>());		
		when(categoryService.getCategoryById(any(UUID.class))).thenReturn(null);	
	}
	
	@After
	public void tearDown() throws Exception {
		this.badSessionFactory = null;
		this.jdbcTemplate = null;
	}	
	
	@Test(expected = HibernateException.class)
	@DirtiesContext
	@SuppressWarnings("unused")
	public void testGetContactWithBadSessionFactory() {
		ReflectionTestUtils.setField(categoryDao, "sessionFactory", badSessionFactory);
		List<Category> c = categoryDao.findCategoryByName("qwer");
	}
	
	@Test
	public void testGetCategory() throws Exception {
		mockMvc.perform(get("/admin/catalog/category/"))
		.andExpect(status().isOk())
		.andExpect(view().name("/admin/catalog/category"));
	}
	
	@Test
	public void testCategoryEdit_CatNotExists_NoAjax() throws Exception {
		Category cat = new Category();
		when(categoryService.getCategoryById(any(UUID.class))).thenReturn(cat);
		mockMvc.perform(get("/admin/catalog/category/edit").param("categoryID", cat.getId().toString()).header("X-Requested-With", ""))
		.andExpect(status().isOk())
		.andExpect(model().attribute("category", cat))
		.andExpect(view().name("/admin/catalog/categoryEdit"));
		
	}
	
	@Test
	public void testCategoryEdit_CatNotExists_Ajax() throws Exception {
		Category cat = new Category();
		when(categoryService.getCategoryById(any(UUID.class))).thenReturn(cat);
		mockMvc.perform(get("/admin/catalog/category/edit")
				        .param("categoryID", cat.getId().toString())
				        .header("X-Requested-With", "XMLHttpRequest"))
		.andExpect(status().isOk())
		.andExpect(model().attribute("category", cat))
		.andExpect(view().name("/admin/catalog/categoryEdit :: editCategoryForm"));
		
	}
	
	@Test
	public void testCategoryEdit_CatExists_NoAjax() throws Exception {
		Category cat = new Category();
		Category cat1 = new Category();
		when(categoryService.getCategoryById(any(UUID.class))).thenReturn(cat);
		mockMvc.perform(get("/admin/catalog/category/edit")
				        .param("categoryID", cat.getId().toString())
				        .flashAttr("category", cat1)
				        .header("X-Requested-With", ""))
		.andExpect(status().isOk())
		.andExpect(model().attribute("category", cat1))
		.andExpect(view().name("/admin/catalog/categoryEdit"));
		
	}	
}
