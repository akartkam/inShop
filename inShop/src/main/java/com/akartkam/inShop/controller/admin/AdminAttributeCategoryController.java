package com.akartkam.inShop.controller.admin;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.UUID;

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


import com.akartkam.inShop.domain.product.attribute.AttributeCategory;
import com.akartkam.inShop.service.product.AttributeCategoryService;


@Controller
@RequestMapping("/admin/catalog/attributecategory")
public class AdminAttributeCategoryController {
	
	  @Autowired
	  AttributeCategoryService attributeCategoryService;
	  
	  @ModelAttribute("allAttributeCategories")
	  public List<AttributeCategory> getAllAttributeCategories() {
	      return attributeCategoryService.getAllAttributeCategory();
	  }	  
 
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "name", "enabled"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });			
	  }
	
	  
	  @RequestMapping(method=GET)
	  public String category(Model model) {
		  return "/admin/attributeCategory"; 
		  }	  
	  
   
	  @RequestMapping("/edit")
	  public String categoryEdit(@RequestParam(value = "categoryID", required = false) String categoryID, Model model) {
		  if(!model.containsAttribute("attributeCategory")) {
			 AttributeCategory category = attributeCategoryService.getAttributeCategoryById(UUID.fromString(categoryID));
		     model.addAttribute("attributeCategory", category);
		  }
          return "/admin/attributeCategoryEdit";		  
		  }	  

	  @RequestMapping("/add")
	  public String categoryAdd(Model model) {
		  AttributeCategory category = new AttributeCategory();
 	      model.addAttribute("attributeCategory", category);
          return "/admin/attributeCategoryEdit";		  
		  }	  

	  
	  @RequestMapping("/delete")
	  public String categoryDelete(@RequestParam(value = "categoryID", required = false) String categoryID, Model model) {
		  attributeCategoryService.softDeleteAttributeCategoryById(UUID.fromString(categoryID));
          return "redirect:/admin/catalog/attributecategory";		  
		  }	  
	  

	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String saveCategory(
			                         @ModelAttribute @Valid AttributeCategory category,
			                         final BindingResult bindingResult,
			                         final RedirectAttributes ra
			                         ) {
	        if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("attributeCategory", category);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.attributeCategory", bindingResult);
	            return "redirect:/admin/catalog/attributecategory/edit";
	        }
	        attributeCategoryService.mergeWithExistingAndUpdateOrCreate(category);
	        return "redirect:/admin/catalog/attributecategory";
	    }
	  
}