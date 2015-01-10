package com.akartkam.inShop.service.product;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.product.CategoryDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.service.AbstractServiceTest;

@TransactionConfiguration(defaultRollback=false)
public class CategoryServiceTest extends AbstractServiceTest {
	
	final static Logger logger = Logger.getLogger(CategoryServiceTest.class);
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryDAO categoryDAO;
	

	@Test
	public void addCategoryTest(){
		jdbcTemplate.execute("delete from Category where name like 'AutoTestAdd%'");
		logger.info("*********Begin AutoTestAdd*********");
		int size = categoryDAO.list().size();
		createTestCategory("AutoTestAdd");
		assertTrue (size < categoryDAO.list().size());
		logger.info("*********End AutoTestAdd*********");
	}
	
	@Test
	public void updateCategoryTest(){
		jdbcTemplate.execute("delete from Category where name like 'AutoTestUpdate%'");
		logger.info("*********Begin AutoTestUpdate*********");
		createTestCategory("AutoTestUpdate");
		Category found = categoryDAO.findCategoryByName("AutoTestUpdate").get(0);
		found.setName("AutoTestUpdate_updated");
		categoryDAO.update(found);
		Category found1 = categoryDAO.findCategoryByName("AutoTestUpdate_updated").get(0);
		assertEquals("AutoTestUpdate_updated", found1.getName());
		logger.info("*********End AutoTestUpdate*********");

	}
	
	@Test
	public void findeCategoryTest(){
		logger.info("*********Begin AutoTestFinde*********");
		Category found = categoryService.getCategoryByName("Test_Root_Category1").get(0);
		assertNotNull(found);
		assertEquals("Test_Root_Category1", found.getName());
		logger.info("*********End AutoTestFinde*********");
	}
	
	
	@Test
	public void buildCategoryHierarchyTest(){
		logger.info("*********Begin buildCategoryHierarchyTest*********");
		Category found = categoryService.getCategoryByName("Test_Category4").get(0);
		List<Category> hCategory = null;
		hCategory  = found.buildCategoryHierarchy(hCategory);
		assertEquals(3, hCategory.size());
		assertTrue(hCategory.contains(found));
		Category c3 = hCategory.get(hCategory.indexOf(found));
		Category c2 = c3.getParent();
		assertNotNull(c2);
		assertTrue(hCategory.contains(c2));
		Category c1 = c2.getParent();
		assertNotNull(c1);
		assertTrue(hCategory.contains(c1));
		logger.info("*********End buildCategoryHierarchyTest*********");
	}

	@Test
	public void subCategoryTest(){
		logger.info("*********Begin subCategoryTest*********");
		Category found = categoryService.getCategoryByName("Test_Root_Category1").get(0);
		List<Category> subCategory = null;
		subCategory  = found.getSubCategory();
		assertEquals(1, subCategory.size());
		assertEquals(found, subCategory.get(0).getParent());
		List<Category> subCategory1  = subCategory.get(0).getSubCategory();
		assertEquals(1, subCategory1.size());
		assertEquals(subCategory.get(0), subCategory1.get(0).getParent());		
		logger.info(subCategory);
		logger.info(subCategory1);
		logger.info("*********End subCategoryTest*********");
	}
	
	@Test
	public void allProductsTest(){
		logger.info("*********Begin allProductsTest*********");
		Category found = categoryService.getCategoryByName("Test_Root_Category1").get(0);
		List<Product> allProducts = null;
		allProducts  = found.getAllProducts(allProducts);
		assertEquals(2, allProducts.size());
		logger.info(allProducts);
		logger.info("*********End allProductsTest*********");
	}
	
	private void createTestCategory(String name) {
		Category ctg = new Category();
		ctg.setName(name);
		ctg.setOrdering(999);
		ctg.setDescription(name+"_description");
		ctg.setLongDescription(name+"_longDescription");	
		categoryService.createCategory(ctg);
	}
	
	
	
}
