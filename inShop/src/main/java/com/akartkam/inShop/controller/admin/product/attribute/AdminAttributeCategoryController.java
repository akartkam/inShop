package com.akartkam.inShop.controller.admin.product.attribute;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;
import com.akartkam.inShop.domain.product.attribute.AttributeType;
import com.akartkam.inShop.formbean.AttributeForm;
import com.akartkam.inShop.service.product.AttributeCategoryService;


@Controller
@RequestMapping("/admin/catalog/attributecategory")
public class AdminAttributeCategoryController {
	
	  @Autowired
	  AttributeCategoryService attributeCategoryService;
	  
	  @SuppressWarnings("rawtypes")
	  @ModelAttribute("allAttributeCategoriesHierarchy")
	  public List getAllAttributeCategories() {
	      return attributeCategoryService.buildAttributeCategoryHierarchy();
	  }	  
 
	  @SuppressWarnings("rawtypes")
	  @ModelAttribute("allAttributeCategories")
	  public List getAllAttributeCategoriesHierarchy() {
	      return attributeCategoryService.getAllAttributeCategory();
	  }
	  
	  @ModelAttribute("allTypes")
	  public List<AttributeType> getAllTypes() {
	      return Arrays.asList(AttributeType.ALL);
	  }
	  	  
	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "name", "ordering", "enabled", 
					                               "attributeType", "attributeCategory"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });
			binder.registerCustomEditor(AttributeCategory.class, "attributeCategory", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		AttributeCategory ch = attributeCategoryService.loadAttributeCategoryById(UUID.fromString(text), false);
			            setValue(ch);
			    	}
			    }
			    });
			binder.registerCustomEditor(UUID.class, "attributeType", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(AttributeType.valueOf(text));
			    }
			    });			
			
	  }
	
	  
	  @RequestMapping(method=GET)
	  public String category(Model model) {
		  return "/admin/attributeCategory"; 
		  }	  
  
   
	  @RequestMapping("/edit")
	  public String categoryEdit(@RequestParam(value = "ID", required = false) String categoryID, Model model) {
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

	  @RequestMapping("/attribute/add")
	  public String attributeAdd(Model model) {
		  AttributeForm attribute = new AttributeForm();
		  model.addAttribute("attribute",attribute);		  
          return "/admin/attributeEdit";		  
	  }	  

	  
	  @RequestMapping("/attribute/edit")
	  public String attributeEdit(@RequestParam(value = "ID", required = false) String ID, Model model) {
		  if(!model.containsAttribute("attribute")) {
			 AttributeForm attribute = new AttributeForm(attributeCategoryService.getAttributeById(UUID.fromString(ID))); 			 
			 model.addAttribute("attribute", attribute);
		  }
          return "/admin/attributeEdit";		  
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

	   @RequestMapping(value="/attribute/edit", method = RequestMethod.POST )
	   public String saveAttribute(
			                         @ModelAttribute("attribute") @Valid AttributeForm attribute, 
			                         final BindingResult bindingResult,
			                         final RedirectAttributes ra
			                         ) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
	        if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("attribute", attribute);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.attribute", bindingResult);
	            return "redirect:/admin/catalog/attributecategory/attribute/edit";
	        }
	        attributeCategoryService.mergeWithExistingAndUpdateOrCreate(attribute);
	        return "redirect:/admin/catalog/attributecategory";
	    }
	   
	   
}