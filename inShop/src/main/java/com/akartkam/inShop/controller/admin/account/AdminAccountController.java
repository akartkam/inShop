package com.akartkam.inShop.controller.admin.account;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.Role;
import com.akartkam.inShop.domain.UserDetailsAdapter;
import com.akartkam.inShop.formbean.AccountForm;
import com.akartkam.inShop.service.AccountService;
import com.akartkam.inShop.service.RoleService;

@Controller
@RequestMapping("/admin/account/account")
@SessionAttributes("rolesUP")
public class AdminAccountController {
		  
	  private static final Log LOG = LogFactory.getLog(AdminAccountController.class);
	  
	  @Autowired
	  private MessageSource messageSource;
	  
	  @Autowired
	  AccountService accountService;
	  
	  @Autowired
	  RoleService roleService;
	  
	  
	  @ModelAttribute("allAccount")
	  public List<Account> getAllAccounts() {
	      return accountService.getAllAccount();
	  }	  
	  
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
	
	  @PreAuthorize("hasRole('ADMIN')")
	  @RequestMapping(method=GET)
	  public String account() {
		  return "/admin/account/account"; 
		  }	  
	  
	  
	  @RequestMapping(value="/userprofile/edit", method=GET)
	  public String userprofileEdit( @RequestHeader(value = "X-Requested-With", required = true) String requestedWith,
			  				 Model model) {		  
		  if (!"XMLHttpRequest".equals(requestedWith)) throw new IllegalStateException("The userprofileEdit method can be called only via ajax!");
		  UserDetailsAdapter uda =  (UserDetailsAdapter)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		  AccountForm account = new AccountForm(uda.getAccount());
		  model.addAttribute("account", account);
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  if (!authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
			  model.addAttribute("rolesUP", account.getRoles());
		  }
			 return "/admin/account/accountEdit :: editForm"; 
		}	  

	  @PreAuthorize("hasRole('ADMIN')")
	  @RequestMapping(value="/reload", method=GET)
	  public String accountReload( @RequestHeader(value = "X-Requested-With", required = true) String requestedWith,
			  				 Model model) {		  
		  if (!"XMLHttpRequest".equals(requestedWith)) throw new IllegalStateException("The accountReload method can be called only via ajax!");
			 return "/admin/account/account :: accountDataTable"; 
		}

	  @PreAuthorize("hasRole('ADMIN')")
	  @RequestMapping("/edit")
	  public String accountEdit(@RequestParam(value = "ID", required = true) String accountID, Model model,
			   					 @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  if(!model.containsAttribute("account")) {
			 Account account = accountService.getAccountById(UUID.fromString(accountID));
		     model.addAttribute("account", new AccountForm(account));
		  }
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/account/accountEdit :: editForm";
            }		  
          return "/admin/account/accountEdit";		  
		  }	  

	  @PreAuthorize("hasRole('ADMIN')")
	  @RequestMapping("/add")
	  public String accountAdd(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  Account account = new AccountForm();
 	      model.addAttribute("account", account);
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/account/accountEdit :: editForm";
            } 	      
          return "/admin/account/accountEdit";		  
		  }	  
	  
	  @PreAuthorize("hasRole('ADMIN')")
	  @RequestMapping(value="/delete", method = RequestMethod.POST)
	  public String accountDelete(@RequestParam(value = "ID", required = false) String accountID, 
			                       @RequestParam(value = "phisycalDelete", required = false) Boolean phisycalDelete,
				                   final RedirectAttributes ra) {
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
          boolean err=false;
		  if (phisycalDelete != null && phisycalDelete)  {
			  Account account = accountService.getAccountById(UUID.fromString(accountID));
			  if(account.canRemove() && authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
				  try {
				    accountService.deleteAccount(account);
				  } catch (DataIntegrityViolationException e) {
					  LOG.error(e);
					  err = true;
					  
				  }
			  } else {
				  err = true;
			  }
			  if (err) {
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", 
				            new String[] {messageSource.getMessage("admin.account.account", null, Locale.getDefault())} , null));
		          ra.addAttribute("error", true);
			  }

		  } else {
		      accountService.softDeleteAccountById(UUID.fromString(accountID));
		  }
          return "redirect:/admin/account/account";		  
		  }	  
	  

	   @SuppressWarnings("unchecked")
	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String saveAccount(    @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			                         final Model model,
			                         final RedirectAttributes ra,
			                         @ModelAttribute("account") @Valid AccountForm account,
			                         final BindingResult bindingResult,
			                         SessionStatus status
			                         ) {
		    if (model.containsAttribute("rolesUP")) {
		    	Set<Role> sr = (Set<Role>)model.asMap().get("rolesUP");
		    	account.setRoles(sr);
		    }
		    accountService.mergeWithExistingAndUpdateOrCreate(account, bindingResult);
	        if (bindingResult.hasErrors()) {
	        	if ("XMLHttpRequest".equals(requestedWith)) {
		        	model.addAttribute("account", account);
		        	return "/admin/account/accountEdit :: editAccountForm"; 	        		
	        	} else {
		        	ra.addFlashAttribute("account", account);
		        	ra.addFlashAttribute("org.springframework.validation.BindingResult.account", bindingResult);
		            return "redirect:/admin/account/account/edit?ID="+account.getId();
	        	}
	        }
	        status.setComplete();
	        if ("XMLHttpRequest".equals(requestedWith)) return "/admin/Ok"; else return "redirect:/admin/account/account";
	    } 
	   

}