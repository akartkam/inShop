package com.akartkam.inShop.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.*;

import com.akartkam.inShop.common.AbstractTest;
import com.akartkam.inShop.controller.order.CartController;
import com.akartkam.inShop.formbean.CartForm;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.util.Constants;
import com.akartkam.inShop.validator.CartItemAddValidator;
import com.akartkam.inShop.validator.CartItemUpdateValidator;

public class CartControllerTest extends AbstractTest {
	
	@Mock
	private ProductService productService;
	
	@Mock
	private CartItemAddValidator cartItemAddValidator;

	@Mock
	private CartItemUpdateValidator cartItemUpdateValidator;	
	
	@Autowired
	private MessageSource messageSource;
	
	@InjectMocks
	private CartController cartController;
	private MockMvc mockMvc;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

	}
	
	@Test
	public void cartTest() throws Exception {
		mockMvc.perform(get("/cart/"))
		.andExpect(status().isOk())
		//.andExpect(request().sessionAttribute(Constants.CART_BEAN_NAME, org.mockito.Matchers.   (CartForm.class)))
		.andExpect(model().attributeExists("cartForm"))
		.andExpect(view().name("cart/cart"));		
	}

}
