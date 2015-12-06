package com.akartkam.inShop.controller.admin.account;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;
import com.akartkam.inShop.service.AccountService;
import com.akartkam.inShop.service.product.AttributeCategoryService;
import com.akartkam.inShop.service.product.CategoryService;

@Controller
@RequestMapping("/admin/account/account")
public class AdminAccountController {
		  
	  @Autowired
	  private MessageSource messageSource;
	  
	  @Autowired
	  AccountService accountService;
	  
	  @ModelAttribute("allAccount")
	  public List<Account> getAllAccounts() {
	      return accountService.getAllAccount();
	  }	  
	  
	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "name", "parent", "url",
					"description", "longDescription", "ordering", "enabled", "*attributesForForm*"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });
	  }
	
	  
	  @RequestMapping(method=GET)
	  public String category() {
		  return "/admin/account/account"; 
		  }	  
	  
	   
	  @RequestMapping("/edit")
	  public String categoryEdit(@RequestParam(value = "categoryID", required = true) String categoryID, Model model,
			   					 @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  if(!model.containsAttribute("category")) {
//			 Category category = categoryService.getCategoryById(UUID.fromString(categoryID));
//		     model.addAttribute("category", category);
		  }
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/categoryEdit :: editCategoryForm";
            }		  
          return "/admin/catalog/categoryEdit";		  
		  }	  

	  @RequestMapping("/add")
	  public String categoryAdd(@RequestParam(value = "categoryID", required = false) String copyCategoryID, Model model,
				                @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws CloneNotSupportedException {
		  Category category;
//		  if (copyCategoryID != null && !"".equals(copyCategoryID)) category = categoryService.cloneCategoryById(UUID.fromString(copyCategoryID)); 
//		  else category = new Category();
// 	      model.addAttribute("category", category);
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/categoryEdit :: editCategoryForm";
            } 	      
          return "/admin/catalog/categoryEdit";		  
		  }	  

	  
	  @RequestMapping(value="/delete", method = RequestMethod.POST)
	  public String categoryDelete(@RequestParam(value = "categoryID", required = false) String categoryID, 
			                       @RequestParam(value = "phisycalDelete", required = false) Boolean phisycalDelete,
				                   final RedirectAttributes ra) {
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  if (phisycalDelete != null && phisycalDelete)  {
	/*		  Category category = categoryService.loadCategoryById(UUID.fromString(categoryID), false);
			  if(category.canRemove() && authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
				  categoryService.deleteCategory(category);   
			  } else {
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", 
						            new String[] {messageSource.getMessage("admin.catalog.category", null, Locale.getDefault())} , null));
				  ra.addAttribute("error", true);
			  }
*/
		  } else {
//		      categoryService.softDeleteCategoryById(UUID.fromString(categoryID));
		  }
          return "redirect:/admin/catalog/category";		  
		  }	  
	  

	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String saveCategory(   @ModelAttribute @Valid Category category,
			                         final BindingResult bindingResult,
			                         final RedirectAttributes ra
			                         ) {
	        if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("category", category);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.category", bindingResult);
	            return "redirect:/admin/catalog/category/edit?categoryID="+category.getId();
	        }
//	        categoryService.mergeWithExistingAndUpdateOrCreate(category);
	        return "redirect:/admin/catalog/category";
	    }
	  
}