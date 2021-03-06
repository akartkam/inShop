package com.akartkam.inShop.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.akartkam.inShop.common.AbstractTest;
import com.akartkam.inShop.dao.product.CategoryDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;
import com.akartkam.inShop.domain.product.attribute.AttributeType;
import com.akartkam.inShop.domain.product.attribute.SimpleAttributeFactory;
import com.akartkam.inShop.service.product.AttributeCategoryService;
import com.akartkam.inShop.service.product.CategoryService;
import com.akartkam.inShop.service.product.ProductService;

public class CategoryServiceTest extends AbstractTest {
	
	private static final Log LOG = LogFactory.getLog(CategoryServiceTest.class);
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private AttributeCategoryService attributeCategoryService;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private ProductService productService; 
	
	@Test
	public void addCategoryTest(){
		logger.info("*********Begin AutoTestAdd*********");
		int size = categoryDAO.list().size();
		Category ct = createTestCategory("AutoTestAdd");
		UUID id = ct.getId();
		assertTrue (size < categoryDAO.list().size());
		sessionFactory.getCurrentSession().flush();
		ct = categoryService.getCategoryById(id);
		assertNotNull(ct);
		assertEquals("AutoTestAdd", ct.getName());
		logger.info("*********End AutoTestAdd*********");
	}
	
	@Test
	public void updateCategoryTest(){
		logger.info("*********Begin AutoTestUpdate*********");
		List<Category> lc = categoryDAO.findCategoryByName("Test_Root_Category1");
		assertNotNull(lc);
		assertTrue(lc.size() > 0);
		Category found = lc.get(0);
		assertNotNull(found);
		Assume.assumeNotNull(found);
		found.setName("AutoTestUpdate_updated");
		categoryDAO.update(found);
		sessionFactory.getCurrentSession().flush();			
		Category found1 = categoryDAO.findCategoryByName("AutoTestUpdate_updated").get(0);
		assertNotNull(found1);
		Assume.assumeNotNull(found1);		
		assertEquals("AutoTestUpdate_updated", found1.getName());
		logger.info("*********End AutoTestUpdate*********");
	}
	
	@Test
	public void findeCategoryTest(){
		logger.info("*********Begin AutoTestFinde*********");
		List<Category> lc = categoryDAO.findCategoryByName("Test_Root_Category1");
		assertNotNull(lc);
		assertTrue(lc.size() > 0);
		Category found = lc.get(0);
		assertNotNull(found);
		assertEquals("Test_Root_Category1", found.getName());
		logger.info("*********End AutoTestFinde*********");
	}
	
	@Test
	public void getRootCategoryHierarchyTest(){
		logger.info("*********Begin getRootCategoryHierarchyTest*********");
		List<Category> rootCategory = categoryService.getRootCategories(true);
		assertNotNull(rootCategory);
		assertTrue(rootCategory.size()>0);
		logger.info("*********End getRootCategoryHierarchyTest*********");		
	}
	
	@Test
	public void buildCategoryHierarchyTest(){
		logger.info("*********Begin buildCategoryHierarchyTest*********");
		List<Category> lc = categoryDAO.findCategoryByName("Test_Category3");
		assertNotNull(lc);
		assertTrue(lc.size() > 0);
		Category found = lc.get(0);
		List<Category> hCategory = null;
		hCategory  = found.buildCategoryHierarchy(hCategory, true);
		assertEquals(2, hCategory.size());
		assertTrue(hCategory.contains(found));
		Category c3 = hCategory.get(hCategory.indexOf(found));
		Category c2 = c3.getParent();
		assertNotNull(c2);
		assertTrue(hCategory.contains(c2));
		logger.info("*********End buildCategoryHierarchyTest*********");
	}
	
	@Test
	public void buildSubCategoryHierarchyTest(){
		logger.info("*********Begin buildSubCategoryHierarchyTest*********");
		List<Category> lc = categoryDAO.findCategoryByName("Test_Root_Category1");
		assertNotNull(lc);
		assertTrue(lc.size() > 0);
		Category found = lc.get(0);
		List<Category> hCategory = null;
		hCategory  = found.buildSubCategoryHierarchy(hCategory, false);
		assertEquals(2, hCategory.size());
		assertTrue(hCategory.contains(found));
		logger.info(hCategory);
		logger.info("*********End buildSubCategoryHierarchyTest*********");
	}	

	@Test
	public void subCategoryTest(){
		logger.info("*********Begin subCategoryTest*********");
		Category found = categoryService.getCategoryByName("Test_Root_Category1").get(0);
		List<Category> subCategory = null;
		subCategory  = found.getSubCategory();
		assertEquals(1, subCategory.size());
		assertEquals(found, subCategory.get(0).getParent());
		logger.info("*********End subCategoryTest*********");
	}
	
	@Test
	public void allProductsTest(){
		logger.info("*********Begin allProductsTest*********");
		Category found = categoryService.getCategoryById(UUID.fromString("b2b0d75c-9dfb-45ee-ada2-a61d7f890780"));
		List<Product> allProducts = null;
		allProducts  = found.getAllProducts(allProducts);
		assertTrue(allProducts.size() > 0);
		logger.info(allProducts);
		logger.info("*********End allProductsTest*********");
	}
	
	@Test
	public void addAttributeTest() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		logger.info("*********Begin addAttributeTes*********");
		Category found = categoryService.getCategoryByName("Test_Category3").get(0);
		AttributeCategory attributeCategory =  attributeCategoryService.getAttributeCategoryById(UUID.fromString("a08bb3cd-907d-45ea-b5b6-eaf4b0b17887")); 
		attributeCategory.setName("Some attribute category");
		AbstractAttribute attribute = SimpleAttributeFactory.createAttribute(AttributeType.DECIMAL);
		attribute.setName("AutoTestAddAttributeDecimal1");
		attribute.setAttributeCategory(attributeCategory);
		found.addAttribute(attribute);
		sessionFactory.getCurrentSession().flush();
		Category found1 = categoryService.getCategoryByName("Test_Category3").get(0);
		Set<AbstractAttribute> foundAttributes = found1.getAttributes();
		Iterator<AbstractAttribute> foundAttributeIter = foundAttributes.iterator();
		while (foundAttributeIter.hasNext()) 
		  assertEquals("AutoTestAddAttributeDecimal1", foundAttributeIter.next().getName());
		logger.info("*********End addAttributeTes*********");
	}
	
	@Test
	public void addProductTest() {
		logger.info("*********Begin addProductTest*********");
		Category found = categoryService.getCategoryByName("Test_Category3").get(0);
		Product product = new Product();
		product.setModel("Model1");
		found.addProduct(product);
		sessionFactory.getCurrentSession().flush();
		Category found1 = categoryService.getCategoryByName("Test_Category3").get(0);
		Product product1 = new Product();
		product1.setModel("Model2");
		found1.addProduct(product1);
		sessionFactory.getCurrentSession().flush();
		Category found2 = categoryService.getCategoryByName("Test_Category3").get(0);
		Product fproduct1 = productService.getProductById(product.getId());
		Product fproduct2 = productService.getProductById(product1.getId());
		assertEquals(found2, fproduct1.getCategory());
		assertEquals(found2, fproduct2.getCategory());
		logger.info("*********End addProductTest*********");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void addAttributeValueTest() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		logger.info("*********Begin addAttributeDecimalValueTest*********");	
		Category found = categoryService.getCategoryByName("Test_Category3").get(0);
		AbstractAttribute attribute = SimpleAttributeFactory.createAttribute(AttributeType.DECIMAL);
		attribute.setName("AutoTestAddAttributeDecimalValue1");	
		AbstractAttribute attribute1 = SimpleAttributeFactory.createAttribute(AttributeType.STRING);
		attribute1.setName("AutoTestAddAttributeDecimalValue2");
		AbstractAttributeValue attributeValue1 = SimpleAttributeFactory.createAttributeValue(AttributeType.STRING);
		attributeValue1.setValue("AutoTestAddAttributeStringValue1");		
		//found.addAttribute(attribute1);
		Product product = new Product();
		//product.setName("AutoTestAddAttributeDecimalValue1");
		//product.setManufacturer("Manufacturer1");
		product.setModel("Model1");	
		AbstractAttributeValue attributeValue = SimpleAttributeFactory.createAttributeValue(AttributeType.DECIMAL);
		attributeValue.setValue(11D);
		product.addAttributeValue(attributeValue, attribute);
		product.addAttributeValue(attributeValue1, attribute1);
		//found.addAttribute(attribute);
		//found.addAttribute(attribute1);
		found.addProduct(product);
		categoryDAO.update(found);
		logger.info("*********End addAttributeDecimalValueTest*********");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void addAttributeSListValueTest() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		logger.info("*********Begin AutoTestAddAttributeSListValue*********");	
		Category found = categoryService.getCategoryByName("Test_Category3").get(0);
		AbstractAttribute attribute = SimpleAttributeFactory.createAttribute(AttributeType.SLIST);
		attribute.setName("AutoTestAddAttributeSListValue1");	
		AbstractAttributeValue attributeValue = SimpleAttributeFactory.createAttributeValue(AttributeType.SLIST);
		Product product = new Product();
		//product.setName("AutoTestAddAttributeSListValue1");
		//product.setManufacturer("Manufacturer1");
		product.setModel("Model1");	
		product.addAttributeValue(attributeValue, attribute);
		//found.addAttribute(attribute);
		found.addProduct(product);
		categoryDAO.update(found);
		logger.info("*********End AutoTestAddAttributeSListValue*********");
	}
	
	@Test
	public void getAllAttributesTest() {
		logger.info("*********Begin getAllAttributesTest*********");	
		Category found = categoryService.getCategoryByName("Test_Category3").get(0);
		List<AbstractAttribute> la = new ArrayList<AbstractAttribute>(); 
		la = found.getAllAttributes(la, true);
		System.out.println(la);
		logger.info("*********End getAllAttributesTest*********");
		
	}

	
	private Category createTestCategory(String name) {
		Category ctg = new Category();
		ctg.setName(name);
		ctg.setOrdering(999);
		ctg.setDescription(name+"_description");
		ctg.setLongDescription(name+"_longDescription");	
		ctg = categoryService.createCategory(ctg);
		return ctg;
	}
	
	
	
}
