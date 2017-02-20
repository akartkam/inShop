package com.akartkam.inShop.controller.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.akartkam.inShop.formbean.CartForm;
import com.akartkam.inShop.formbean.CheckoutForm;
import com.akartkam.inShop.util.CartUtil;
import com.akartkam.inShop.util.Constants;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
	private static final Log LOG = LogFactory.getLog(CheckoutController.class);
	
	private static String checkoutView = "order/checkout";
	@RequestMapping
	public String checkout(HttpServletRequest request, HttpServletResponse response, Model model,
						  @ModelAttribute("checkoutForm") CheckoutForm checkoutForm) {
        CartForm cart = CartUtil.getCartFromSession(request, false);
        if (cart != null) {
            model.addAttribute(Constants.CART_BEAN_NAME, cart);
            model.addAttribute("isShortMode", new Boolean(true));
    		            
        }
        return getCheckoutView();
	}
	
	public String getCheckoutView() {
		return this.checkoutView;
	}

}
