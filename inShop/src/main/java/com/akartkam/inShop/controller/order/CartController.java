package com.akartkam.inShop.controller.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.akartkam.inShop.formbean.CartForm;
import com.akartkam.inShop.util.CartUtil;

//@Controller("/cart")
public class CartController {
	private static final Log LOG = LogFactory.getLog(CartController.class);
	private static String cartView = "cart/cart";

	private static String cartPageRedirect = "redirect:/cart";
	
	@RequestMapping("")
    public String cart(HttpServletRequest request, HttpServletResponse response, Model model) {
        CartForm cart = CartUtil.getCartFromSession(request);
        if (cart != null) {
            model.addAttribute("cartForm", cart);
        }
        return getCartView();
    }
	
	public static String getCartView() {
		return cartView;
	}

	public static String getCartPageRedirect() {
		return cartPageRedirect;
	}	

}
