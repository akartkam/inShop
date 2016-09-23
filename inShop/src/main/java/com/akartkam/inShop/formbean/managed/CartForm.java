package com.akartkam.inShop.formbean.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.akartkam.inShop.formbean.CartItemForm;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class CartForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2629554330145347921L;
	private List<CartItemForm> cartItems = new ArrayList<CartItemForm>();
}
