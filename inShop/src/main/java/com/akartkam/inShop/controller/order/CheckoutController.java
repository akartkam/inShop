package com.akartkam.inShop.controller.order;

import java.beans.PropertyEditorSupport;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.domain.order.Delivery;
import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.order.Store;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.exception.AddToCartException;
import com.akartkam.inShop.exception.InventoryUnavailableException;
import com.akartkam.inShop.exception.ProductNotFoundException;
import com.akartkam.inShop.exception.RequiredAttributeNotProvidedException;
import com.akartkam.inShop.exception.SkuNotFoundException;
import com.akartkam.inShop.formbean.Buy1clickForm;
import com.akartkam.inShop.formbean.CartForm;
import com.akartkam.inShop.formbean.CartItemForm;
import com.akartkam.inShop.formbean.CheckoutForm;
import com.akartkam.inShop.service.EmailInfo;
import com.akartkam.inShop.service.EmailService;
import com.akartkam.inShop.service.PdfService;
import com.akartkam.inShop.service.order.DeliveryService;
import com.akartkam.inShop.service.order.OrderService;
import com.akartkam.inShop.service.product.CategoryService;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.util.CartUtil;
import com.akartkam.inShop.util.Constants;
import com.akartkam.inShop.validator.Buy1clickFormValidator;
import com.akartkam.inShop.validator.CartItemValidator;
import com.akartkam.inShop.validator.CheckoutFormValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@Autowired
	protected CategoryService categoryService;

	@Autowired
	private CartItemValidator cartItemValidator;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private Buy1clickFormValidator buy1clicFormValidator;
	
	@Value("#{appProperties['mail.smtp.from']}")
    private String mailFrom;	
	
	
	@Autowired
	private PdfService pdfService;
   
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
		binder.registerCustomEditor(Sku.class, "sku", new PropertyEditorSupport() {
		    @Override
		    public void setAsText(String text) {
		    	if (!"".equals(text)) {
		    		Sku s = productService.loadSkuById(UUID.fromString(text), false);
		            setValue(s);
		    	}    
		    }
		});
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		
		
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
		LOG.info("Requesting for placing order, email="+checkoutForm.getEmail());
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
			LOG.error("",e);
			ra.addFlashAttribute("e", e);
			ra.addFlashAttribute("errCode", 500);
			ra.addFlashAttribute("errMessage", messageSource.getMessage("error.default.clientMessage", null, null));
			return "redirect:/error-default";
		}
		CartUtil.removeCartFromSession(request);
		orderService.refresh(order);
		EmailInfo ei = new EmailInfo();
		ei.setEmailTemplate("mail/order-confirmation");
		ei.setFromAddress(mailFrom);
		ei.setSubject("Заказ №"+order.getOrderNumber());
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("order", order);
		emailService.sendSimpleMail(request, response, order.getEmailAddress() , ei, vars);
		model.addAttribute("order", order);
		List<Category> rootCategorys = categoryService.getRootCategories(false);
		model.addAttribute("rootCategorys", rootCategorys);
		LOG.info("Request for placing order complite");
		return "/order/order-success";
	}
	
	@RequestMapping(value = "/buy1click")
    public String buy1click(HttpServletRequest request, HttpServletResponse response, 
    						@ModelAttribute("cartItemForm") CartItemForm cartItemForm,				
    						final Model model,
    		                final BindingResult bindingResult ) throws IOException {
		LOG.info("Requesting for placing buy1click, skuId="+cartItemForm.getSkuId());
    	Map<String, Object> responseMap = new HashMap<String, Object>();
    	Map<String, String> errorsMap = new HashMap<String, String>();
    	responseMap.put("type", "buy1click");
    	try {
        	cartItemValidator.validate(cartItemForm, bindingResult);
        	if (!bindingResult.hasErrors()) {
        		model.addAttribute("sku", cartItemForm.getSku());
        		model.addAttribute("imageUrl", cartItemForm.getImageUrl());
        		model.addAttribute("buy1clickForm", new Buy1clickForm());
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
    	if (!errorsMap.isEmpty()){
    		responseMap.put("errors", errorsMap);
    		model.addAttribute("errors", new Boolean(true));    		
    	}
    	if (!responseMap.isEmpty()){
    		model.addAttribute("responseMap", responseMap);
    		model.addAttribute("ajaxExtraData", new ObjectMapper().writeValueAsString(responseMap));
    	}
    	if (errorsMap.size() == 0) LOG.info("Request for placing buy1click complite");
    	return "/order/partials/buy1click";
	}
	
	@RequestMapping(value = "/place-buy1click")
    public String placeBuy1click(HttpServletRequest request, HttpServletResponse response, 
    						@ModelAttribute("buy1clickForm") Buy1clickForm buy1clickForm,				
    						final Model model,
    		                final BindingResult bindingResult ) throws IOException {
		buy1clicFormValidator.validate(buy1clickForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("sku", buy1clickForm.getSku());
			model.addAttribute("imageUrl", buy1clickForm.getSku().lookupImages());
			model.addAttribute("org.springframework.validation.BindingResult.buy1clickForm", bindingResult);
			return "/order/partials/buy1click";
    	} else {
    		try {
    			orderService.placeBuy1click(buy1clickForm);
    		} catch (Exception e) {
    			LOG.error("",e);
    			return "/order/partials/buy1click-fail";
    		}
    		return "/order/partials/buy1click-success";
    	}
	    	
	}
	
	@RequestMapping(value="/test-order-confirm")
	public String testOrderConfirm(HttpServletRequest request, HttpServletResponse response, Model model) throws MessagingException {
		Order order = orderService.getOrderById(UUID.fromString("0ad1ca81-5064-476f-8d8e-cd46fb9ff680"));
		model.addAttribute("order", order);
		return "order-confirmation";
	}

	
	@RequestMapping(value="/test-email")
	public String testEmail(HttpServletRequest request, HttpServletResponse response) throws MessagingException {
		Order order = orderService.getOrderById(UUID.fromString("0ad1ca81-5064-476f-8d8e-cd46fb9ff680"));
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("order", order);
		EmailInfo emailInfo = new EmailInfo();
		emailInfo.setFromAddress(mailFrom);
		emailInfo.setSubject("Test subject");
		emailInfo.setEmailTemplate("mail/order-confirmation");
		emailService.sendSimpleMail(request, response, "akchurin_artur@mail.ru", emailInfo, vars);
		return "redirect:/";
	}
	
	
	@RequestMapping(value="/test-pdf")
	public ResponseEntity<byte[]> testPdf(HttpServletRequest request, HttpServletResponse response) throws MessagingException {
		Order order = orderService.getOrderById(UUID.fromString("3cebe8c3-5348-4867-b131-f0b4a3ba319c"));
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("order", order);	
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		pdfService.generatePdf(out, request, response, "pdf/orderPdf", vars);		
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    String filename = "output.pdf";
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> resp = new ResponseEntity<byte[]>(out.toByteArray(), headers, HttpStatus.OK);
	    return resp;
	}
	
	public String getCheckoutView() {
		return this.checkoutView;
	}

}
 