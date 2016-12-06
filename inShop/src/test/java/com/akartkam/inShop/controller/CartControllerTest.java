package com.akartkam.inShop.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.hamcrest.core.Is;

import com.akartkam.inShop.common.AbstractTest;
import com.akartkam.inShop.controller.order.CartController;
import com.akartkam.inShop.dao.product.ProductDAO;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.formbean.CartForm;
import com.akartkam.inShop.formbean.CartItemForm;
import com.akartkam.inShop.service.order.InventoryService;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.util.Constants;
import com.akartkam.inShop.validator.CartItemValidator;
import com.akartkam.inShop.validator.CartItemUpdateValidator;

public class CartControllerTest extends AbstractTest {
	
	@Mock
	private Product productMock;
	
	@Mock
	private ProductOption productOptionMock;
	
	@Mock
	private ProductService productService;
	
	@Mock
	private InventoryService inventoryService;
	
	@Spy @InjectMocks
	private CartItemValidator cartItemAddValidator;

	@Mock
	private CartItemUpdateValidator cartItemUpdateValidator;	
	
	@InjectMocks
	private CartController cartController;
	private MockMvc mockMvc;

	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Spy
	private MessageSource messageSource;
	
	@Before
	public void setUp() throws Exception {
		messageSource = (MessageSource) applicationContext.getBean("messageSource");
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
		        .andExpect(content().contentType("application/json;charset=UTF-8"))
		        .andExpect(jsonPath("$.errors").doesNotExist())
		        .andExpect(jsonPath("$.productName", is("Panasonic RP-HX550")))
		        .andExpect(jsonPath("$.quantityAdded", is(1)))
		        .andExpect(jsonPath("$.cartItemCount", is(1)))
		        .andDo(new ResultHandler() {
			                @Override
			                public void handle(MvcResult result) throws Exception {
			                	CartForm cart = (CartForm) result.getRequest().getSession().getAttribute(Constants.CART_BEAN_NAME);
			                	assertNotNull(cart);
			                	assertTrue(cart.getCartItemsCount()==1);
			                	CartItemForm cartItem = cart.getCartItems().get(0);
			                	assertNotNull(cartItem.getSkuId());
			                	assertNotEquals(cartItem.getSkuId(), "");
			                	assertTrue("159c5c82-3b8f-4473-a965-046604f8e56d".equals(cartItem.getProductId()));
			                	assertTrue(cartItem.getQuantity() == 1);
			                	assertTrue("Panasonic RP-HX550".equals(cartItem.getProductName()));
			                }
		        		})		
		        .andDo(print());
		
	}

	@Test 
	public void cartAddJsonTest_bad_quantity() throws Exception {
		Product product = productDAO.get(UUID.fromString("159c5c82-3b8f-4473-a965-046604f8e56d"));
		when(productService.getProductById(any(UUID.class))).thenReturn(product);
		when(inventoryService.isQuantityAvailable(any(Sku.class), anyInt())).thenReturn(true);	
		mockMvc.perform(get("/cart/add")
				.accept(MediaType.APPLICATION_JSON)
		        .param("productId", "159c5c82-3b8f-4473-a965-046604f8e56d").param("quantity", "-1").param("itemAttributes['ColorBWG']", "Черный")
		        .header("X-Requested-With", "XMLHttpRequest")
		        .header("charset", "utf-8"))
		        .andExpect(status().isOk())
		        .andExpect(content().contentType("application/json;charset=UTF-8"))
		        .andExpect(jsonPath("$.errors").exists())
		        .andExpect(jsonPath("$.errors.quantity", is("Неверное количество: -1")))
		        .andExpect(jsonPath("$.productName").doesNotExist())
		        .andExpect(jsonPath("$.quantityAdded").doesNotExist())
		        .andExpect(jsonPath("$.cartItemCount").doesNotExist())
		        .andDo(new ResultHandler() {
			                @Override
			                public void handle(MvcResult result) throws Exception {
			                	CartForm cart = (CartForm) result.getRequest().getSession().getAttribute(Constants.CART_BEAN_NAME);
			                	assertNull(cart);
			                }
		        		})		        
		        .andDo(print());
		
	}
	
	@Test
	public void cartAddJson_productNotFound() throws Exception {
		when(productService.getProductById(any(UUID.class))).thenReturn(null);
		when(inventoryService.isQuantityAvailable(any(Sku.class), anyInt())).thenReturn(true);	
		mockMvc.perform(get("/cart/add")
				.accept(MediaType.APPLICATION_JSON)
		        .param("productId", "159c5c82-3b8f-4473-a965-046604f8e56d").param("quantity", "1").param("itemAttributes['ColorBWG']", "Черный")
		        .header("X-Requested-With", "XMLHttpRequest")
		        .header("charset", "utf-8"))
		        .andExpect(status().isOk())
		        .andExpect(content().contentType("application/json;charset=UTF-8"))
		        .andExpect(jsonPath("$.errors").exists())
		        .andExpect(jsonPath("$.errors.criticalError", is("productNotFound")))
		        .andExpect(jsonPath("$.productName").doesNotExist())
		        .andExpect(jsonPath("$.quantityAdded").doesNotExist())
		        .andExpect(jsonPath("$.cartItemCount").doesNotExist())		        
		        .andDo(new ResultHandler() {
			                @Override
			                public void handle(MvcResult result) throws Exception {
			                	CartForm cart = (CartForm) result.getRequest().getSession().getAttribute(Constants.CART_BEAN_NAME);
			                	assertNull(cart);
			                }
		        		})		        
		        .andDo(print());		
	}
	
	@Test
	public void cartAddJson_RequiredAttributeNotProvidedException() throws Exception {
		when(productService.getProductById(any(UUID.class))).thenReturn(productMock);
		when(inventoryService.isQuantityAvailable(any(Sku.class), anyInt())).thenReturn(true);	
		when(productMock.getId()).thenReturn(UUID.fromString("159c5c82-3b8f-4473-a965-046604f8e56d"));
		when(productOptionMock.getRequired()).thenReturn(true);
		when(productOptionMock.getName()).thenReturn("Some Product Option");
		Set<ProductOption> sp = new HashSet<ProductOption>();
		sp.add(productOptionMock);
		when(productMock.getProductOptions()).thenReturn(sp);
		mockMvc.perform(get("/cart/add")
				.accept(MediaType.APPLICATION_JSON)
		        .param("productId", "159c5c82-3b8f-4473-a965-046604f8e56d").param("quantity", "1").param("itemAttributes['ColorBWG']", "Черный")
		        .header("X-Requested-With", "XMLHttpRequest")
		        .header("charset", "utf-8"))
		        .andExpect(status().isOk())
		        .andExpect(content().contentType("application/json;charset=UTF-8"))
		        .andExpect(jsonPath("$.errors").exists())
		        .andExpect(jsonPath("$.errors.criticalError", is("allOptionsRequired")))
		        .andExpect(jsonPath("$.productName").doesNotExist())
		        .andExpect(jsonPath("$.quantityAdded").doesNotExist())
		        .andExpect(jsonPath("$.cartItemCount").doesNotExist())		        
		        .andDo(new ResultHandler() {
			                @Override
			                public void handle(MvcResult result) throws Exception {
			                	CartForm cart = (CartForm) result.getRequest().getSession().getAttribute(Constants.CART_BEAN_NAME);
			                	assertNull(cart);
			                }
		        		})		        
		        .andDo(print());		
	}	

	@Test
	public void cartAddJson_skuNotFound() throws Exception {
		when(productService.getProductById(any(UUID.class))).thenReturn(productMock);
		when(inventoryService.isQuantityAvailable(any(Sku.class), anyInt())).thenReturn(true);	
		when(productMock.getId()).thenReturn(UUID.fromString("159c5c82-3b8f-4473-a965-046604f8e56d"));
		when(productMock.getDefaultSku()).thenReturn(null);
		when(productOptionMock.getRequired()).thenReturn(false);
		when(productOptionMock.getName()).thenReturn("Some Product Option");
		mockMvc.perform(get("/cart/add")
				.accept(MediaType.APPLICATION_JSON)
		        .param("productId", "159c5c82-3b8f-4473-a965-046604f8e56d").param("quantity", "1").param("itemAttributes['ColorBWG']", "Черный")
		        .header("X-Requested-With", "XMLHttpRequest")
		        .header("charset", "utf-8"))
		        .andExpect(status().isOk())
		        .andExpect(content().contentType("application/json;charset=UTF-8"))
		        .andExpect(jsonPath("$.errors").exists())
		        .andExpect(jsonPath("$.errors.criticalError", is("skuNotFound")))
		        .andExpect(jsonPath("$.productName").doesNotExist())
		        .andExpect(jsonPath("$.quantityAdded").doesNotExist())
		        .andExpect(jsonPath("$.cartItemCount").doesNotExist())		        
		        .andDo(new ResultHandler() {
			                @Override
			                public void handle(MvcResult result) throws Exception {
			                	CartForm cart = (CartForm) result.getRequest().getSession().getAttribute(Constants.CART_BEAN_NAME);
			                	assertNull(cart);
			                }
		        		})		        
		        .andDo(print());		
	}	

	@Test
	public void cartAddJson_InventoryUnavailableException() throws Exception {
		Product product = productDAO.get(UUID.fromString("159c5c82-3b8f-4473-a965-046604f8e56d"));
		when(productService.getProductById(any(UUID.class))).thenReturn(product);
		when(inventoryService.isQuantityAvailable(any(Sku.class), anyInt())).thenReturn(false);	
		mockMvc.perform(get("/cart/add")
				.accept(MediaType.APPLICATION_JSON)
		        .param("productId", "159c5c82-3b8f-4473-a965-046604f8e56d").param("quantity", "1").param("itemAttributes['ColorBWG']", "Черный")
		        .header("X-Requested-With", "XMLHttpRequest")
		        .header("charset", "utf-8"))
		        .andExpect(status().isOk())
		        .andExpect(content().contentType("application/json;charset=UTF-8"))
		        .andExpect(jsonPath("$.errors").exists())
		        .andExpect(jsonPath("$.errors.criticalError", is("inventoryUnavailable")))
		        .andExpect(jsonPath("$.productName").doesNotExist())
		        .andExpect(jsonPath("$.quantityAdded").doesNotExist())
		        .andExpect(jsonPath("$.cartItemCount").doesNotExist())		        
		        .andDo(new ResultHandler() {
			                @Override
			                public void handle(MvcResult result) throws Exception {
			                	CartForm cart = (CartForm) result.getRequest().getSession().getAttribute(Constants.CART_BEAN_NAME);
			                	assertNull(cart);
			                }
		        		})		        
		        .andDo(print());		
	}	
	
	@Test
	public void cartRemoveItem_expect_successful_ajax() throws Exception {
		CartForm cart = new CartForm();
		CartItemForm cartItem = new CartItemForm();
		cartItem.setProductId("159c5c82-3b8f-4473-a965-046604f8e56d");
		cartItem.setSkuId("0fa89e22-46e1-4b68-acb9-d5df82f37823");
		cartItem.setQuantity(1);
		cart.addCartItem(cartItem);
		mockMvc.perform(get("/cart/remove")
		        .param("productId", "159c5c82-3b8f-4473-a965-046604f8e56d")
		        .param("skuId","0fa89e22-46e1-4b68-acb9-d5df82f37823")
		        .param("quantity", "1")
		        .param("itemAttributes['ColorBWG']", "Черный")
		        .header("X-Requested-With", "XMLHttpRequest")
		        .sessionAttr(Constants.CART_BEAN_NAME, cart))
		        .andExpect(status().isOk())
		        .andExpect(request().sessionAttribute(Constants.CART_BEAN_NAME, cart))
		        .andExpect(model().attributeDoesNotExist("errors"))
		        .andExpect(model().attributeExists("extradata"))
		        .andExpect(model().attribute("extradata", "{\"productId\":\"159c5c82-3b8f-4473-a965-046604f8e56d\",\"cartItemCount\":0}"))
		        .andExpect(view().name("cart/cart"))
		        .andDo(new ResultHandler() {
			                @Override
			                public void handle(MvcResult result) throws Exception {
			                	CartForm cart = (CartForm) result.getRequest().getSession().getAttribute(Constants.CART_BEAN_NAME);
			                	assertTrue(cart.getCartItems().isEmpty());
			                }
		        		});
		
	}	
	
	@Test
	public void cartUpdateItem_succsess_zero_quantity_ajax() throws Exception {	
		CartForm cart = new CartForm();
		CartItemForm cartItem = new CartItemForm();
		cartItem.setProductId("159c5c82-3b8f-4473-a965-046604f8e56d");
		cartItem.setSkuId("0fa89e22-46e1-4b68-acb9-d5df82f37823");
		cartItem.setQuantity(1);
		cart.addCartItem(cartItem);
		mockMvc.perform(get("/cart/updateQuantity")
		        .param("productId", "159c5c82-3b8f-4473-a965-046604f8e56d")
		        .param("skuId","0fa89e22-46e1-4b68-acb9-d5df82f37823")
		        .param("quantity", "0")
		        .param("itemAttributes['ColorBWG']", "Черный")
		        .header("X-Requested-With", "XMLHttpRequest")
		        .sessionAttr(Constants.CART_BEAN_NAME, cart))
		        .andExpect(status().isOk())
		        .andExpect(request().sessionAttribute(Constants.CART_BEAN_NAME, cart))
		        .andExpect(model().attributeDoesNotExist("errors"))
		        .andExpect(model().attributeExists("extradata"))
		        .andExpect(model().attribute("extradata", "{\"productId\":\"159c5c82-3b8f-4473-a965-046604f8e56d\",\"cartItemCount\":0}"))
		        .andExpect(view().name("cart/cart"))
		        .andDo(new ResultHandler() {
			                @Override
			                public void handle(MvcResult result) throws Exception {
			                	CartForm cart = (CartForm) result.getRequest().getSession().getAttribute(Constants.CART_BEAN_NAME);
			                	assertTrue(cart.getCartItems().isEmpty());
			                }
		        		});
		
	}
	
	@Test
	public void cartUpdateItem_succsess_ajax() throws Exception {	
		when(inventoryService.isQuantityAvailable(any(Sku.class), anyInt())).thenReturn(false);	
		CartForm cart = new CartForm();
		CartItemForm cartItem = new CartItemForm();
		cartItem.setProductId("159c5c82-3b8f-4473-a965-046604f8e56d");
		cartItem.setSkuId("0fa89e22-46e1-4b68-acb9-d5df82f37823");
		cartItem.setQuantity(1);
		cart.addCartItem(cartItem);
		mockMvc.perform(get("/cart/updateQuantity")
		        .param("productId", "159c5c82-3b8f-4473-a965-046604f8e56d")
		        .param("skuId","0fa89e22-46e1-4b68-acb9-d5df82f37823")
		        .param("quantity", "2")
		        .param("itemAttributes['ColorBWG']", "Черный")
		        .header("X-Requested-With", "XMLHttpRequest")
		        .sessionAttr(Constants.CART_BEAN_NAME, cart))
		        .andExpect(status().isOk())
		        .andExpect(request().sessionAttribute(Constants.CART_BEAN_NAME, cart))
		        .andExpect(model().attributeDoesNotExist("errors"))
		        .andExpect(model().attributeExists("extradata"))
		        .andExpect(model().attribute("extradata", "{\"productId\":\"159c5c82-3b8f-4473-a965-046604f8e56d\",\"cartItemCount\":1}"))
		        .andExpect(view().name("cart/cart"))
		        .andDo(new ResultHandler() {
			                @Override
			                public void handle(MvcResult result) throws Exception {
			                	CartForm cart = (CartForm) result.getRequest().getSession().getAttribute(Constants.CART_BEAN_NAME);
			                	assertNotNull(cart);
			                	assertTrue(cart.getCartItems().size() == 1);
			                	assertTrue(cart.getCartItems().get(0).getQuantity() == 2);
			                }
		        		});
		
	}


}
