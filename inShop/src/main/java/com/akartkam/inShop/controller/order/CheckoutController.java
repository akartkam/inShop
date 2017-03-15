package com.akartkam.inShop.controller.order;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.domain.order.Delivery;
import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.order.Store;
import com.akartkam.inShop.exception.PlaceOrderException;
import com.akartkam.inShop.formbean.CartForm;
import com.akartkam.inShop.formbean.CheckoutForm;
import com.akartkam.inShop.service.EmailInfo;
import com.akartkam.inShop.service.EmailService;
import com.akartkam.inShop.service.order.DeliveryService;
import com.akartkam.inShop.service.order.OrderService;
import com.akartkam.inShop.util.CartUtil;
import com.akartkam.inShop.util.Constants;
import com.akartkam.inShop.validator.CheckoutFormValidator;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
	private static final Log LOG = LogFactory.getLog(CheckoutController.class);
	
	@Autowired
	private DeliveryService deliveryService;
	
	@Autowired
	private CheckoutFormValidator checkoutFormValidator;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private OrderService orderService;
	
	@Value("#{appProperties['mail.smtp.from']}")
    private String mailFrom;	

	private static String checkoutView = "order/checkout";

	@ModelAttribute("AllDeliveries")
	public List<Delivery> getAllDeliveries(){
		List<Delivery> ret = new ArrayList<Delivery>();
		for (Delivery d : deliveryService.getAllDeliverys()) {
			if (d.isEnabled() && d.getIsPublic()) {
				ret.add(d);
			}
		}
		return ret;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Delivery.class, "delivery", new PropertyEditorSupport() {
		    @Override
		    public void setAsText(String text) {
		    	if (!"".equals(text)) {
		    		Delivery dv = deliveryService.loadDeliveryById(UUID.fromString(text), false);
		            setValue(dv);
		    	}    
		    }
		});
		
		binder.registerCustomEditor(Store.class, "store", new PropertyEditorSupport() {
		    @Override
		    public void setAsText(String text) {
		    	if (!"".equals(text)) {
		    		Store st = deliveryService.getStoreById(UUID.fromString(text));
		            setValue(st);
		    	}    
		    }
		}); 
		
	}
	
	@RequestMapping
	public String checkout(HttpServletRequest request, HttpServletResponse response, Model model) {
        CartForm cart = CartUtil.getCartFromSession(request, false);
        if (cart != null && cart.getCartItemsCount() > 0) {
            model.addAttribute(Constants.CART_BEAN_NAME, cart);
            model.addAttribute("isShortMode", new Boolean(true));
            if(!model.containsAttribute("checkoutForm")) {
            	model.addAttribute("checkoutForm", new CheckoutForm());
            }
    		            
        }
        return getCheckoutView();
	}
	
	@RequestMapping(value="/place-order", method = RequestMethod.POST)
	public String placeOrder (HttpServletRequest request, HttpServletResponse response, Model model,
							  @Valid @ModelAttribute("checkoutForm") CheckoutForm checkoutForm, 
			  				  final BindingResult bindingResult,
			  				  final RedirectAttributes ra) {
		checkoutFormValidator.validate(checkoutForm, bindingResult);
		if (bindingResult.hasErrors()) {
        	ra.addFlashAttribute("checkoutForm", checkoutForm);
        	ra.addFlashAttribute("org.springframework.validation.BindingResult.checkoutForm", bindingResult);
            return "redirect:/checkout";
        }
		Order order = null;
		try {
			CartForm cart = CartUtil.getCartFromSession(request, false);
			order = orderService.placeOrder(checkoutForm, cart);
		} catch (Exception e) {
			LOG.error(e);
			return "redirect:/";
		}
		CartUtil.removeCartFromSession(request);
		EmailInfo ei = new EmailInfo();
		ei.setEmailTemplate("/mail/order-confirmation");
		ei.setFromAddress(mailFrom);
		ei.setSubject("Заказ №"+order.getOrderNumber());
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("order", order);
		emailService.sendSimpleMail(request, response, order.getEmailAddress() , ei, vars);
		model.addAttribute("order", order);
		return "redirect:/";
	}
	
	@RequestMapping(value="/test-order-confirm")
	public String testOrderConfirm(HttpServletRequest request, HttpServletResponse response, Model model) throws MessagingException {
		Order order = orderService.getOrderById(UUID.fromString("1203fb64-be23-4d7f-96a0-9f488a65c664"));
		model.addAttribute("order", order);
		return "order-confirmation";
	}

	
	@RequestMapping(value="/test-email")
	public String testEmail(HttpServletRequest request, HttpServletResponse response) throws MessagingException {
		Order order = orderService.getOrderById(UUID.fromString("1203fb64-be23-4d7f-96a0-9f488a65c664"));
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("order", order);
		EmailInfo emailInfo = new EmailInfo();
		emailInfo.setFromAddress(mailFrom);
		emailInfo.setSubject("Test subject");
		emailInfo.setEmailTemplate("order-confirmation");
		emailService.sendSimpleMail(request, response, "akartkam@gmail.com", emailInfo, vars);
		return "redirect:/";
	}
	
	public String getCheckoutView() {
		return this.checkoutView;
	}

}
 