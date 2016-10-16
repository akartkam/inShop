package com.akartkam.inShop.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.hamcrest.core.Is;

import com.akartkam.inShop.common.AbstractTest;
import com.akartkam.inShop.controller.order.CartController;
import com.akartkam.inShop.dao.product.ProductDAO;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.formbean.CartForm;
import com.akartkam.inShop.service.order.InventoryService;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.util.Constants;
import com.akartkam.inShop.validator.CartItemAddValidator;
import com.akartkam.inShop.validator.CartItemUpdateValidator;

public class CartControllerTest extends AbstractTest {
	
	@Mock
	private ProductService productService;
	
	@Mock
	private InventoryService inventoryService;
	
	@Spy @InjectMocks
	private CartItemAddValidator cartItemAddValidator;

	@Mock
	private CartItemUpdateValidator cartItemUpdateValidator;	
	
	@InjectMocks
	private CartController cartController;
	private MockMvc mockMvc;

	@Autowired
	private ProductDAO productDAO;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

	}
	
	@Test
	public void cartTest() throws Exception {
		mockMvc.perform(get("/cart/"))
		.andExpect(status().isOk())
		.andExpect(request().sessionAttribute(Constants.CART_BEAN_NAME, Is.isA(CartForm.class)))
		.andExpect(model().attributeExists("cartForm"))
		.andExpect(view().name("cart/cart"));		
	}
	
	@Test
	public void cartAddJsonTest_expect_successful() throws Exception {
		Product product = productDAO.get(UUID.fromString("159c5c82-3b8f-4473-a965-046604f8e56d"));
		when(productService.getProductById(any(UUID.class))).thenReturn(product);
		when(inventoryService.isQuantityAvailable(any(Sku.class), anyInt())).thenReturn(true);		
		mockMvc.perform(get("/cart/add")
				.accept(MediaType.APPLICATION_JSON)
		        .param("productId", "159c5c82-3b8f-4473-a965-046604f8e56d").param("quantity", "1").param("itemAttributes['ColorBWG']", "Черный")
		        .header("X-Requested-With", "XMLHttpRequest"))
		        .andExpect(status().isOk())
		        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		        .andExpect(jsonPath("$.productName", is("Panasonic RP-HX550")))
		        .andExpect(jsonPath("$.quantityAdded", is(1)))
		        .andExpect(jsonPath("$.cartItemCount", is(1)))
		        .andDo(print());
		
	}

}
