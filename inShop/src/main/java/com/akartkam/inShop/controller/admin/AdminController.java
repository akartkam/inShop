package com.akartkam.inShop.controller.admin;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.service.product.CategoryService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	  @Autowired
	  CategoryService categoryService;
 
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "name", "parent.id",
					"description", "longDescription", "enabled"});
			binder.registerCustomEditor(Category.class, "parent", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	Category ch = categoryService.getCategoryById(text);
			        setValue(ch);
			    }
			    });
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
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
	  
	  /*@RequestMapping("/catalog/category/edit/{id}")
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
	   */
	   
	  @RequestMapping("/catalog/category/edit")
	  public String categoryEdit(@RequestParam(value = "categoryID", required = false) String categoryID, Model model) {
		  if(!model.containsAttribute("category")) {
			 Category category = categoryService.getCategoryById(categoryID);
		     model.addAttribute("category", category);
		  }
		  model.addAttribute("allCategories", categoryService.getAllCategoryHierarchy());
          return "/admin/categoryEdit";		  
		  }	  

	  @RequestMapping("/catalog/category/delete")
	  public String categoryDelete(@RequestParam(value = "categoryID", required = false) String categoryID, Model model) {
		  categoryService.deleteCategory(UUID.fromString(categoryID));
		  model.addAttribute("allCategories", categoryService.getAllCategoryHierarchy());
          return "redirect:/admin/catalog/category";		  
		  }	  
	  

	   @RequestMapping(value="/catalog/category/edit", method = RequestMethod.POST )
	   public String saveCategory(
			                         @ModelAttribute @Valid Category category,
			                         final BindingResult bindingResult,
			                         final RedirectAttributes ra
			                         ) {
	        if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("category", category);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.category", bindingResult);
	            return "redirect:/admin/catalog/category/edit";
	        }
	        categoryService.mergeWithExistingAndUpdate(category);
	        return "redirect:/admin/catalog/category";
	    }
	  
}