package com.akartkam.inShop.controller.order;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.akartkam.inShop.exception.AddToCartException;
import com.akartkam.inShop.exception.InventoryUnavailableException;
import com.akartkam.inShop.formbean.CartForm;
import com.akartkam.inShop.formbean.CartItemForm;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.util.CartUtil;

//@Controller("/cart")
public class CartController {
	private static final Log LOG = LogFactory.getLog(CartController.class);
	private static String cartView = "cart/cart";

	private static String cartPageRedirect = "redirect:/cart";
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping("")
    public String cart(HttpServletRequest request, HttpServletResponse response, Model model) {
        CartForm cart = CartUtil.getCartFromSession(request);
        if (cart != null) {
            model.addAttribute("cartForm", cart);
        }
        return getCartView();
    }
	
    @RequestMapping(value = "/add", produces = "application/json")
    public @ResponseBody Map<String, Object> addJson(HttpServletRequest request, HttpServletResponse response, 
    		                                         Model model, @ModelAttribute("cartItemForm") CartItemForm cartItemForm) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
       /* try {

        	if (cartItemForm.getItemAttributes() == null || cartItemForm.getItemAttributes().size() == 0) {
                responseMap.put("productId", cartItemForm.getProductId());
            }
            responseMap.put("productName", productService.getProductById(cartItemForm.getProductId()).getDefaultSku().getName());
            responseMap.put("quantityAdded", cartItemForm.getQuantity());
            responseMap.put("cartItemCount", String.valueOf(CartUtil.getCartFromSession(request).getCartItemsCount()));
        } catch (AddToCartException e) {
            if (e.getCause() instanceof RequiredAttributeNotProvidedException) {
                responseMap.put("error", "allOptionsRequired");
            } else if (e.getCause() instanceof ProductOptionValidationException) {
                ProductOptionValidationException exception = (ProductOptionValidationException) e.getCause();
                responseMap.put("error", "productOptionValidationError");
                responseMap.put("errorCode", exception.getErrorCode());
                responseMap.put("errorMessage", exception.getMessage());
                //blMessages.getMessage(exception.get, lfocale))
            } else if (e.getCause() instanceof InventoryUnavailableException) {
                responseMap.put("error", "inventoryUnavailable");
            } else {
                throw e;
            }
        }*/
        
        return responseMap;
    }	
	
	public static String getCartView() {
		return cartView;
	}

	public static String getCartPageRedirect() {
		return cartPageRedirect;
	}	

}
