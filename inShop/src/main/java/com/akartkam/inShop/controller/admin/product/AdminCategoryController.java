package com.akartkam.inShop.controller.admin.product;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.akartkam.inShop.service.product.AttributeCategoryService;
import com.akartkam.inShop.service.product.CategoryService;

@Controller
@RequestMapping("/admin/catalog/category")
public class AdminCategoryController {
	
	  @Autowired
	  CategoryService categoryService;
	  
	  @Autowired
	  AttributeCategoryService attributeCategoryService;
	  
	  @ModelAttribute("allCategories")
	  public List<Category> getAllCategories() {
	      return categoryService.getAllCategoryHierarchy();
	  }	  
	  
	  @SuppressWarnings("rawtypes")
	  @ModelAttribute("allAttributeCategories")
	  public List getAllAttributeCategoriesHierarchy() {
	      return attributeCategoryService.getAllAttributeCategory();
	  }
 
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "name", "parent",
					"description", "longDescription", "ordering", "enabled"});
			binder.registerCustomEditor(Category.class, "parent", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		Category ch = categoryService.loadCategoryById(UUID.fromString(text), false);
			            setValue(ch);
			    	}    
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
	  public String category(Model model) {
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
	   
	  @RequestMapping("/edit")
	  public String categoryEdit(@RequestParam(value = "categoryID", required = false) String categoryID, Model model) {
		  if(!model.containsAttribute("category")) {
			 Category category = categoryService.getCategoryById(UUID.fromString(categoryID));
		     model.addAttribute("category", category);
		  }
          return "/admin/categoryEdit";		  
		  }	  

	  @RequestMapping("/add")
	  public String categoryAdd(@RequestParam(value = "copyCategoryID", required = false) String copyCategoryID, Model model) throws CloneNotSupportedException {
		  Category category;
		  if (copyCategoryID != null && !"".equals(copyCategoryID)) category = categoryService.cloneCategoryById(UUID.fromString(copyCategoryID)); 
		  else category = new Category();
 	      model.addAttribute("category", category);
          return "/admin/categoryEdit";		  
		  }	  

	  
	  @RequestMapping(value="/delete", method = RequestMethod.POST)
	  public String categoryDelete(@RequestParam(value = "categoryID", required = false) String categoryID, 
			                       @RequestParam(value = "phisycalDelete", required = false) Boolean phisycalDelete,
				                   final RedirectAttributes ra) {
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  if (phisycalDelete != null && phisycalDelete)  {
			  Category category = categoryService.loadCategoryById(UUID.fromString(categoryID), false);
			  if(category.canRemove() && authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
				  categoryService.deleteCategory(category);   
			  } else {
				  ra.addFlashAttribute("errormessage", "Нельзя удалить категорию. Имеются ссылки на другие сущности, либо недостаточно прав.");
				  ra.addAttribute("error", true);
			  }

		  } else {
		      categoryService.softDeleteCategoryById(UUID.fromString(categoryID));
		  }
          return "redirect:/admin/catalog/category";		  
		  }	  
	  

	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String saveCategory(   @RequestParam(value="attributesSelected", required=false) List<String> attributes,
			                         @ModelAttribute @Valid Category category,
			                         final BindingResult bindingResult,
			                         final RedirectAttributes ra
			                         ) {
	        if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("category", category);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.category", bindingResult);
	            return "redirect:/admin/catalog/category/edit";
	        }
	        categoryService.mergeWithExistingAndUpdateOrCreate(category, attributes);
	        return "redirect:/admin/catalog/category";
	    }
	  
}