package com.akartkam.inShop.controller.customer;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;



























import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.controller.admin.product.AdminProductController;
import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.customer.Customer;
import com.akartkam.inShop.formbean.AccountForm;
import com.akartkam.inShop.formbean.CustomerForm;
import com.akartkam.inShop.service.customer.CustomerService;

@Controller
@RequestMapping("/admin/customer/customer")
@SessionAttributes({"customerCreateDate", "customerUsername"})
public class AdminCustomerController {
	  private static final Log LOG = LogFactory.getLog(AdminCustomerController.class);
	 
	  @Autowired
	  CustomerService customerService;
	  
	  @Autowired
	  private MessageSource messageSource;
	  
 
	  @ModelAttribute("allCustomer")
	  public List<Customer> getAllCustomer() {
	      return customerService.getAllCustomer();
	  }
	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "firstName", "lastName", "middleName", "email",
					                               "phone", "address", "birthdate", "password", "confirmPassword", "username"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });	
			binder.registerCustomEditor(java.util.Date.class, "birthdate", 
					 new CustomDateEditor(new SimpleDateFormat(messageSource.getMessage("date.formatshort", null, Locale.getDefault())), true));
			
	  }
	  
	  @RequestMapping(method=GET)
	  public String customer() {
		  return "/admin/customer/customer"; 
	  }	  
	  
	 @RequestMapping("/add")
	  public String customerAdd(@RequestParam(value = "ID", required = false) String copyID, Model model,
				               @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws CloneNotSupportedException {
		  CustomerForm customer = new CustomerForm();
		  //if (copyID != null && !"".equals(copyID)) customer = customerService.cloneBrandById(UUID.fromString(copyID)); 
		  //else brand = new Brand();
 	      model.addAttribute("customer", customer);
 	      model.addAttribute("createAccount",new Boolean(true));
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/customer/customerEdit :: editCustomerForm";
            } 	      
          return "/admin/customer/customerEdit";		  
		  }		  
 
	  @RequestMapping("/edit")
	  public String customerEdit(@RequestParam(value = "ID", required = false) String customerID, Model model,
			   				     @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  if(!model.containsAttribute("customer")) {
			  Customer customer = customerService.getCustomerById(UUID.fromString(customerID));
			  if (customer == null) throw new IllegalStateException("Customer "+customerID+" not found!"); 
			  CustomerForm customerForm = new CustomerForm(customer);
			  model.addAttribute("customer", customerForm);
		      model.addAttribute("customerCreateDate", customerForm.getCreatedDate());
		      if (customerForm.getAccount() != null)
		        model.addAttribute("customerUsername", customerForm.getUsername());
		      
		  }
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/customer/customerEdit :: editCustomerForm";
            }		  
          return "/admin/customer/customerEdit";		  
		  }	  
	  	  
	  
	  @RequestMapping(value="/delete", method = RequestMethod.POST)
	  public String brandDelete(@RequestParam(value = "ID", required = false) String ID, 
			                    @RequestParam(value = "phisycalDelete", required = false) Boolean phisycalDelete,
				                 final RedirectAttributes ra) {
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  if (phisycalDelete != null && phisycalDelete)  {
			  Customer customer = customerService.loadCustomerById(UUID.fromString(ID), false);
			  if(customer.canRemove() && authorities.containsAll(Arrays.asList(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("MANAGER")))) {
				  customerService.deleteCustomer(customer);   
			  } else {
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", new String[] {"����������"} , null));
				  ra.addAttribute("error", true);
			  }

		  } else {
			  customerService.softDeleteCustomerById(UUID.fromString(ID));
		  }
          return "redirect:/admin/customer/customer";		  
		  } 
	 
	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String customerSave( @RequestParam(value = "createAccount", required = false) boolean createAccount,
			                       final Model model,
					               final RedirectAttributes ra,
					               @ModelAttribute("customer") @Valid CustomerForm customer,
					               final BindingResult bindingResult,
					               SessionStatus status
			                         ) throws IllegalAccessException, InvocationTargetException {
		    if (model.containsAttribute("customerCreateDate")) {
		    	DateTime dc = (DateTime)model.asMap().get("customerCreateDate");
		    	customer.setCreatedDate(dc);
		    }
		    if (model.containsAttribute("customerUsername")) {
		    	customer.setUsername((String)model.asMap().get("customerUsername"));
		    }
		   Errors br = customerService.mergeWithExistingAndUpdateOrCreate(customer, bindingResult, createAccount);
		   if (br.hasErrors()) {
	        	ra.addFlashAttribute("customer", customer);
	        	ra.addFlashAttribute("createAccount", new Boolean(createAccount));
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.customer", br);
	            return "redirect:/admin/customer/customer/edit";
	        }
		   status.setComplete();
		   return "redirect:/admin/customer/customer";
	    }
  

}
