package com.akartkam.inShop.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.akartkam.inShop.formbean.CartForm;

public class CartUtil {
	
	private CartUtil() {}
	
    public static CartForm getCartFromSession(HttpServletRequest request) {
    	CartForm cartForm = (CartForm) request.getSession().getAttribute(Constants.CART_BEAN_NAME);
        if (cartForm == null) {
        	cartForm = new CartForm();
            request.getSession().setAttribute(Constants.CART_BEAN_NAME, cartForm);
        } 
        return cartForm;
    }
    
    public static CartForm getCartFromSession(HttpServletRequest request, boolean create) {
    	HttpSession curSession = request.getSession(false);
    	if (curSession != null) {
    		CartForm cartForm = (CartForm) curSession.getAttribute(Constants.CART_BEAN_NAME);
    		return cartForm;
    	} 
        return null; 
    }
	
    public static void removeCartFromSession(HttpServletRequest request) {
        request.getSession().removeAttribute(Constants.CART_BEAN_NAME);
    }


}
