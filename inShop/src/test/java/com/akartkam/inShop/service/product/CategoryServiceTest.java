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
import com.akartkam.inShop.dao.product.attribute.SListDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.attribute.AttributeDecimalValue;
import com.akartkam.inShop.domain.product.attribute.AttributeType;
import com.akartkam.inShop.domain.product.attribute.SList;
import com.akartkam.inShop.domain.product.attribute.SimpleAttributeFactory;
import com.akartkam.inShop.service.AbstractServiceTest;

@TransactionConfiguration(defaultRollback=false)
public class CategoryServiceTest extends AbstractServiceTest {
	
	final static Logger logger = Logger.getLogger(CategoryServiceTest.class);
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private SListDAO sListDAO;
	
	

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
		assertTrue(allProducts.size() > 0);
		logger.info(allProducts);
		logger.info("*********End allProductsTest*********");
	}
	
	@Test
	public void addAttributeTest() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		logger.info("*********Begin addAttributeTes*********");
/*		jdbcTemplate.execute("delete from Attribute_Decimal_Value where id in (select id from Attribute_Value where attribute_id in " + 
							 "(select id from Attribute where name like 'AutoTestAddAttribute%'))");
		jdbcTemplate.execute("delete from Attribute_String_Value where id in (select id from Attribute_Value where attribute_id in " + 
				 			 "(select id from Attribute where name like 'AutoTestAddAttribute%'))");
		jdbcTemplate.execute("delete from Attribute_SList_Value where id in (select id from Attribute_Value where attribute_id in " + 
				 			 "(select id from Attribute where name like 'AutoTestAddAttribute%'))");
		jdbcTemplate.execute("delete from Attribute_Value where attribute_id in (select id from Attribute where name like 'AutoTestAddAttribute%')");
		jdbcTemplate.execute("delete from Attribute where name like 'AutoTestAddAttribute%'");
*/		
		jdbcTemplate.execute("select del_attribute('AutoTestAddAttribute')");
		Category found = categoryService.getCategoryByName("Test_Category4").get(0);
		AbstractAttribute attribute = SimpleAttributeFactory.createAttribute(AttributeType.DECIMAL);
		attribute.setName("AutoTestAddAttributeDecimal1");
		found.addAttribute(attribute);
		categoryDAO.update(found);
		Category found1 = categoryService.getCategoryByName("Test_Category4").get(0);
		List<AbstractAttribute> foundAttribute = found1.getAttributes();
		assertEquals("AutoTestAddAttributeDecimal1", ((AbstractAttribute) foundAttribute.get(0)).getName());
		logger.info("*********End addAttributeTes*********");
	}
	
	@Test
	public void addProductTest() {
		logger.info("*********Begin addProductTest*********");
		jdbcTemplate.execute("select del_product('AutoTestAddProduct')");
		Category found = categoryService.getCategoryByName("Test_Category4").get(0);
		Product product = new Product();
		product.setName("AutoTestAddProduct1");
		product.setManufacturer("Manufacturer1");
		product.setModel("Model1");
		found.addProduct(product);
		categoryDAO.update(found);
		Category found1 = categoryService.getCategoryByName("Test_Category5").get(0);
		Product product1 = new Product();
		product1.setName("AutoTestAddProduct2");
		product1.setManufacturer("Manufacturer2");
		product1.setModel("Model2");
		found1.addProduct(product1);
		categoryDAO.update(found1);
		Category found2 = categoryService.getCategoryByName("Test_Category4").get(0);
		Category found3 = categoryService.getCategoryByName("Test_Category5").get(0);
		Product fproduct1 = categoryService.getProductByName("AutoTestAddProduct1").get(0);
		Product fproduct2 = categoryService.getProductByName("AutoTestAddProduct2").get(0);
		assertEquals(found2, fproduct1.getCategory());
		assertEquals(found3, fproduct2.getCategory());
		logger.info("*********End addProductTest*********");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void addAttributeValueTest() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		logger.info("*********Begin addAttributeDecimalValueTest*********");	
		jdbcTemplate.execute("select del_attribute('AutoTestAddAttributeDecimalValue')");
		jdbcTemplate.execute("select del_product('AutoTestAddAttributeDecimalValue')");
		Category found = categoryService.getCategoryByName("Test_Category4").get(0);
		AbstractAttribute attribute = SimpleAttributeFactory.createAttribute(AttributeType.DECIMAL);
		attribute.setName("AutoTestAddAttributeDecimalValue1");	
		AbstractAttribute attribute1 = SimpleAttributeFactory.createAttribute(AttributeType.STRING);
		attribute1.setName("AutoTestAddAttributeDecimalValue2");
		AbstractAttributeValue attributeValue1 = SimpleAttributeFactory.createAttributeValue(AttributeType.STRING);
		attributeValue1.setAttributeValue("AutoTestAddAttributeStringValue1");		
		found.addAttribute(attribute1);
		Product product = new Product();
		product.setName("AutoTestAddAttributeDecimalValue1");
		product.setManufacturer("Manufacturer1");
		product.setModel("Model1");	
		AbstractAttributeValue attributeValue = SimpleAttributeFactory.createAttributeValue(AttributeType.DECIMAL);
		attributeValue.setAttributeValue(11D);
		product.addAttributeValue(attributeValue, attribute);
		product.addAttributeValue(attributeValue1, attribute1);
		found.addAttribute(attribute);
		found.addAttribute(attribute1);
		found.addProduct(product);
		categoryDAO.update(found);
		logger.info("*********End addAttributeDecimalValueTest*********");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void addAttributeSListValueTest() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		logger.info("*********Begin AutoTestAddAttributeSListValue*********");	
		jdbcTemplate.execute("select del_attribute('AutoTestAddAttributeSListValue')");
		jdbcTemplate.execute("select del_product('AutoTestAddAttributeSListValue')");
		Category found = categoryService.getCategoryByName("Test_Category4").get(0);
		AbstractAttribute attribute = SimpleAttributeFactory.createAttribute(AttributeType.SLIST);
		attribute.setName("AutoTestAddAttributeSListValue1");	
		SList sList1 = new SList();
		sList1.setValue("sList1Value1");
		sListDAO.update(sList1);
		SList sList2 = new SList();
		sList2.setValue("sList1Value2");
		sListDAO.update(sList2);
		AbstractAttributeValue attributeValue = SimpleAttributeFactory.createAttributeValue(AttributeType.SLIST);
		attributeValue.setAttributeValue(sList1);	
		Product product = new Product();
		product.setName("AutoTestAddAttributeSListValue1");
		product.setManufacturer("Manufacturer1");
		product.setModel("Model1");	
		product.addAttributeValue(attributeValue, attribute);
		found.addAttribute(attribute);
		found.addProduct(product);
		categoryDAO.update(found);
		logger.info("*********End AutoTestAddAttributeSListValue*********");
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
