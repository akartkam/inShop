package com.akartkam.inShop.controller.admin.account;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.UserDetailsAdapter;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.formbean.AccountForm;
import com.akartkam.inShop.service.AccountService;
import com.akartkam.inShop.service.AccountServiceImpl;
import com.akartkam.inShop.service.RoleService;
import com.akartkam.inShop.service.product.AttributeCategoryService;
import com.akartkam.inShop.service.product.CategoryService;

@Controller
@RequestMapping("/admin/account/userprofile")
public class AdminUserProfileController {
		  
	  private static final Log LOG = LogFactory.getLog(AdminUserProfileController.class);
	  
	  @Autowired
	  private MessageSource messageSource;
	  
	  @Autowired
	  AccountService accountService;
	  
	  @Autowired
	  RoleService roleService;
	    
	  
	  @ModelAttribute("allRoles")
	  public List<Role> getAllRoles() {
	      return roleService.getAllRoles();
	  }
	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "username", "firstName", "lastName", "middleName", "email", "phone", 
					                               "address","rolesList*", "password", "confirmPassword", "createdDate", "enabled"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });
			binder.registerCustomEditor(Role.class,"rolesList", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		Role rl =  roleService.getRoleById(UUID.fromString(text)); 
			            setValue(rl);
			    	}			    
			    }
			    });			
	  }
	
	  
	  @RequestMapping(method=GET)
	  public String account(Model model) {		  
		  UserDetailsAdapter uda =  (UserDetailsAdapter)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		  AccountForm account = new AccountForm(uda.getAccount());
		  model.addAttribute("account", account);
		  model.addAttribute("action", "/admin/account/userprofile/edit");		  
		  return "/admin/account/userprofile"; 
		  }	  
	  	  

	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String saveAccount(   @ModelAttribute @Valid AccountForm account,
			                         final BindingResult bindingResult,
			                         final RedirectAttributes ra
			                         ) {
		    accountService.mergeWithExistingAndUpdateOrCreate(account, bindingResult);
	        if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("account", account);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.account", bindingResult);
	        }
            return "redirect:/logout";
	    }  
}