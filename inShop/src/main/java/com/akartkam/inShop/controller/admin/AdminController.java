package com.akartkam.inShop.controller.admin;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.spring.support.Layout;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.service.product.CategoryService;

@Controller
@RequestMapping("/admin")
@Layout("layouts/admin-default")
public class AdminController {
	
	  @Autowired
	  CategoryService categoryService;
 
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "name", "parent",
					"description", "longDescription", "enabled"});
			binder.registerCustomEditor(Category.class, "parent", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	Category ch = categoryService.getCategoryById(text);
			        setValue(ch);
			    }
			    });
	  }

	
	  @RequestMapping(method=GET)
	  public String admin(HttpSession  session) {
		  session.setAttribute("isadmin", new Boolean(true));
		  return "admin/admin"; 
		  }
	  
	  @RequestMapping("/catalog/category")
	  public String category(Model model) {
		  model.addAttribute("allCategories", categoryService.getAllCategoryHierarchy()); 
		  return "/admin/category"; 
		  }	  
	  
	  @Layout("disable")
	  @RequestMapping("/catalog/category/edit/{id}")
	  public String category(@PathVariable("id") String id, Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  if(!model.containsAttribute("category")) {
			 Category category = categoryService.getCategoryById(id);
		     model.addAttribute("category", category);
		  }
		  model.addAttribute("allCategories", categoryService.getAllCategoryHierarchy());
          if ("XMLHttpRequest".equals(requestedWith)) {
            return "/admin/categoryEdit :: editCategoryForm";
          }
          return "/admin/category";		  
		    
		  }	  
	   
	   @Layout("disable")
	   @RequestMapping(value="/catalog/category/edit", method = RequestMethod.POST )
	   public String saveCategory(
			                         @Valid @ModelAttribute("category") Category category,
			                         final BindingResult bindingResult,
			                         final RedirectAttributes ra) {
	        if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("errorId", category.getId().toString());
	        	ra.addFlashAttribute("category", category);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.category", bindingResult);
	            return "redirect:/admin/catalog/category/edit/"+category.getId().toString();
	        }
	        categoryService.updateCategory(category);
	        return "redirect:/admin/catalog/category";
	    }
	  
}