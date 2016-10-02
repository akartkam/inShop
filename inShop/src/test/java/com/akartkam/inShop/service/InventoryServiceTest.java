package com.akartkam.inShop.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.constraints.AssertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;

import org.mockito.MockitoAnnotations;

import com.akartkam.inShop.common.AbstractTest;
import com.akartkam.inShop.dao.product.SkuDAO;
import com.akartkam.inShop.domain.product.InventoryType;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.service.order.InventoryService;
import com.akartkam.inShop.service.order.InventoryServiceImpl;

public class InventoryServiceTest extends AbstractTest {
	private static final Log LOG = LogFactory.getLog(InventoryServiceTest.class);
	
	@Mock
	private Sku sku1;
	
	@Mock
	private SkuDAO skuDAO;
	
	@InjectMocks
	private InventoryService inventoryService = new InventoryServiceImpl();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Map<UUID, Integer> mapUUID = new HashMap<UUID, Integer>();
		mapUUID.put(sku1.getId(), 2);
		when(skuDAO.findMapSkuIdQuantityAvailable(Matchers.<Collection<Sku>>any())).thenReturn(mapUUID);
		when(sku1.getQuantityAvailable()).thenReturn(2);
		when(sku1.getInventoryType()).thenReturn(InventoryType.CHECK_QUANTITY);
		when(sku1.isAvailable()).thenReturn(true);
		when(sku1.isDefaultSku()).thenReturn(true);
	}
	
	@Test
	public void retrieveQuantitiesAvailable_check_quantity_Test() {
		Map<Sku, Integer> resMap = inventoryService.retrieveQuantitiesAvailable(Arrays.asList(sku1));
		assertNotNull(resMap);
		assertEquals(resMap.size(), 1);
		assertEquals((Integer)resMap.get(sku1), new Integer(2));
	}
	
	@Test
	public void retrieveQuantitiesAvailable_always_available_Test() {
		when(sku1.getInventoryType()).thenReturn(InventoryType.ALWAYS_AVAILABLE);
		Map<Sku, Integer> resMap = inventoryService.retrieveQuantitiesAvailable(Arrays.asList(sku1));
		assertNotNull(resMap);
		assertEquals(resMap.size(), 1);
		assertNull((Integer)resMap.get(sku1));
	}	
	
	@Test(expected=IllegalArgumentException.class)
	public void isQuantityAvailable_bad_quantity_Test() {
		inventoryService.isQuantityAvailable(sku1, 0);
	}
	
	@Test
	public void isQuantityAvailable_success_Test() {
		assertTrue(inventoryService.isQuantityAvailable(sku1, 2));
	}	
}
