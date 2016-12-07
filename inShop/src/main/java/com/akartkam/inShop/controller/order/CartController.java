package com.akartkam.inShop.controller.order;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.akartkam.inShop.exception.AddToCartException;
import com.akartkam.inShop.exception.InventoryUnavailableException;
import com.akartkam.inShop.exception.ProductNotFoundException;
import com.akartkam.inShop.exception.RequiredAttributeNotProvidedException;
import com.akartkam.inShop.exception.SkuNotFoundException;
import com.akartkam.inShop.exception.UpdateCartException;
import com.akartkam.inShop.formbean.CartForm;
import com.akartkam.inShop.formbean.CartItemForm;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.util.CartUtil;
import com.akartkam.inShop.validator.CartItemValidator;
import com.akartkam.inShop.validator.CartItemUpdateValidator;

import static com.akartkam.inShop.util.CommonUtil.isAjaxRequest;;

@Controller
@RequestMapping("/cart")
public class CartController {
	private static final Log LOG = LogFactory.getLog(CartController.class);
	private static String cartView = "cart/cart";

	private static String cartPageRedirect = "redirect:/cart";
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CartItemValidator cartItemValidator;

	@Autowired
	private CartItemUpdateValidator cartItemUpdateValidator;	
	
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping()
    public String cart(HttpServletRequest request, HttpServletResponse response, Model model) {
        CartForm cart = CartUtil.getCartFromSession(request);
        if (cart != null) {
            model.addAttribute("cartForm", cart);
        }
        return getCartView();
    }
	
    @RequestMapping(value = "/add")
    public String addJson(HttpServletRequest request, HttpServletResponse response, final Model model,
    		                                         @ModelAttribute("cartItemForm") CartItemForm cartItemForm,
    		                                         final BindingResult bindingResult ) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        Map<String, String> errorsMap = new HashMap<String, String>();
        try {
        	cartItemValidator.validate(cartItemForm, bindingResult);
        	if (!bindingResult.hasErrors()) {
            	CartForm cart = CartUtil.getCartFromSession(request);
            	cart.addCartItem(cartItemForm);
                responseMap.put("productName", cartItemForm.getProductName());
                responseMap.put("quantityAdded", cartItemForm.getQuantity());
                responseMap.put("cartItemCount", CartUtil.getCartFromSession(request).getCartItemsCount());        		
        	} else {
        		if (bindingResult.hasFieldErrors()) {
        			for (FieldError fe : bindingResult.getFieldErrors()){
        				String errMsg = messageSource.getMessage(fe, null);
	        			errorsMap.put(fe.getField(), errMsg);
	        			LOG.error(errMsg);
        			}
        		}
        	}
        } catch (AddToCartException e) {
        	LOG.error("Runtime exception has occurred during to add to cart", e);
        	if (e.getCause() instanceof ProductNotFoundException){
        		errorsMap.put("criticalError", "productNotFound");
        	} else if (e.getCause() instanceof SkuNotFoundException) {
            	errorsMap.put("criticalError", "skuNotFound");
        	} else if (e.getCause() instanceof RequiredAttributeNotProvidedException) {
            	errorsMap.put("criticalError", "allOptionsRequired");
            } else if (e.getCause() instanceof InventoryUnavailableException) {
            	errorsMap.put("criticalError", "inventoryUnavailable");
            } else {
            	errorsMap.put("criticalError", "criticalRuntimeError");
            }
        }
        if (!errorsMap.isEmpty()) responseMap.put("errors", errorsMap);
        model.addAttribute("responseMap", responseMap);
        return cartView + " :: ajax";
    }
    
    @RequestMapping("/remove")
    public String remove(HttpServletRequest request, HttpServletResponse response, 
    		@ModelAttribute("cartItemForm") CartItemForm cartItemForm, Model model,
    		final BindingResult bindingResult) throws IOException  {
        Map<String, String> errorsMap = new HashMap<String, String>();
    	CartForm cart = CartUtil.getCartFromSession(request);
    	if (!cart.removeCartItem(cartItemForm)){
    		bindingResult.reject("error.remove.cartItemForm");
    		errorsMap.put("error", messageSource.getMessage("error.remove.cartItemForm", null, null));
    		LOG.error("During update zero quantity, could not remove the cart item with productId = "+cartItemForm.getProductId() +" , skuId = "+cartItemForm.getSkuId());
    	} 
        if (isAjaxRequest(request)) {
            Map<String, Object> extraData = new HashMap<String, Object>();
            extraData.put("cartItemCount", cart.getCartItemsCount());
            extraData.put("productId", cartItemForm.getProductId());
            model.addAttribute("extradata", new ObjectMapper().writeValueAsString(extraData));
        	if (errorsMap.size() > 0) model.addAttribute("errors", new ObjectMapper().writeValueAsString(errorsMap));
            return getCartView();
        } else {
            return getCartPageRedirect();
        }
    }
    
    @RequestMapping("/updateQuantity")
    public String updateQuantity(HttpServletRequest request, HttpServletResponse response, Model model,
    							 @ModelAttribute("cartItemForm") CartItemForm cartItemForm,
    		                     final BindingResult bindingResult) throws IOException {
        Map<String, String> errorsMap = new HashMap<String, String>();
    	CartForm cart = CartUtil.getCartFromSession(request);
        if (cartItemForm.getQuantity() == 0) {
        	if (!cart.removeCartItem(cartItemForm)){
        		bindingResult.reject("error.remove.cartItemForm");
        		errorsMap.put("error", messageSource.getMessage("error.remove.cartItemForm", null, null));
        		LOG.error("During update cart item with zero quantity, could not remove the cart item with productId = "+cartItemForm.getProductId() +" , skuId = "+cartItemForm.getSkuId());
        	}
        } else {
        	try {
        		cartItemUpdateValidator.validate(cartItemForm, bindingResult);
        	} catch (UpdateCartException e) {
            	LOG.error("Runtime exception has occurred during update the cart", e);
            	if (e.getCause() instanceof SkuNotFoundException){
            		errorsMap.put("criticalError", "skuNotFound");
            	} else if (e.getCause() instanceof InventoryUnavailableException) {
            		errorsMap.put("criticalError", "inventoryUnavailable");
            	}
        	}
	    	if (!bindingResult.hasErrors()) {
	    		if (!cart.updateQuantityOnCartItem(cartItemForm)) {
	        		bindingResult.reject("error.updateQuantity.cartItemForm");
	        		errorsMap.put("error", messageSource.getMessage("error.updateQuantity.cartItemForm", null, null));
	        		LOG.error("Error occurred during update cart item quantity, productId = "+cartItemForm.getProductId() +" , skuId = "+cartItemForm.getSkuId() +", quantity = "+cartItemForm.getQuantity());	    			
	    		}
	    	}
        }
        if (isAjaxRequest(request)) {
            Map<String, Object> extraData = new HashMap<String, Object>();
            extraData.put("productId", cartItemForm.getProductId());
            extraData.put("cartItemCount", cart.getCartItemsCount());
            model.addAttribute("extradata", new ObjectMapper().writeValueAsString(extraData));
            if (errorsMap.size() > 0) model.addAttribute("errors", new ObjectMapper().writeValueAsString(errorsMap)); 
            return getCartView();
        } else {
            return getCartPageRedirect();
        }
    }    
	
	public static String getCartView() {
		return cartView;
	}

	public static String getCartPageRedirect() {
		return cartPageRedirect;
	}	

}
