package com.akartkam.inShop.controller.admin.product.attribute;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import java.util.Collection;

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

import com.akartkam.inShop.domain.Unit;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;
import com.akartkam.inShop.domain.product.attribute.AttributeType;
import com.akartkam.inShop.domain.product.attribute.AttributeValuesHolderType;
import com.akartkam.inShop.formbean.AttributeForm;
import com.akartkam.inShop.service.product.AttributeCategoryService;

@Controller
@RequestMapping("/admin/catalog/attributecategory")
public class AdminAttributeCategoryController {
	
	  @Autowired
	  AttributeCategoryService attributeCategoryService;
	  
	  @Autowired
	  private MessageSource messageSource;
	  
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
	  	  
	  @ModelAttribute("allUnits")
	  public List<Unit> getAllUnits() {
	      return Arrays.asList(Unit.ALL);
	  }
	  
	  @ModelAttribute("allAttrValuesHolder")
	  public List<AttributeValuesHolderType> getAllAttrValuesHolder() {
	      return Arrays.asList(AttributeValuesHolderType.ALL);
	  }
	  
	  
	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "name", "ordering", "enabled", "unit", "attributeValuesHolder", "code",
					                               "attributeType", "attributeCategory", "items", "createdDate", "isShowOnProductHeader"});
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
			binder.registerCustomEditor(AttributeType.class, "attributeType", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		setValue(AttributeType.valueOf(text));
			    	} else {
			    		setValue(null);
			    	}
			    }
			    });			
			binder.registerCustomEditor(Set.class, "items", new PropertyEditorSupport() {
			    @SuppressWarnings("unchecked")
				@Override
			    public String getAsText() {
			    	StringBuilder content = new StringBuilder();
			    	Object val = getValue();
			    	if (val != null) {
				    	for (String str: (Set<String>)val) {
				    		content.append(str+"\r\n"); 
				    	}
				    	if (content.toString().endsWith("\r\n"))
				    		content.delete(content.length()-2 , content.length());
			    	}
			    	return content.toString();
			    }

				@Override
			    public void setAsText(String text) {
					 Set<String> val = new HashSet<String>(Arrays.asList(text.split("\r\n")));
					 val.remove("");
			    	 setValue(val);
			    }
			    });			
			
	  }
	
	  
	  @RequestMapping(method=GET)
	  public String category() {
		  return "/admin/catalog/attributeCategory"; 
		  }	  
  
   
	  @RequestMapping("/edit")
	  public String categoryEdit(@RequestParam(value = "ID", required = false) String categoryID, Model model,
			  					 @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  if(!model.containsAttribute("attributeCategory")) {
			 AttributeCategory category = attributeCategoryService.getAttributeCategoryById(UUID.fromString(categoryID));
		     model.addAttribute("attributeCategory", category);
		  }
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/attributeCategoryEdit :: editCategoryForm";
            }		  
          return "/admin/catalog/attributeCategoryEdit";		  
		  }	  

	  @RequestMapping("/add")
	  public String categoryAdd(@RequestParam(value = "ID", required = false) String copyID, Model model,
				 @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws CloneNotSupportedException {
		  AttributeCategory category;
		  if (copyID != null && !"".equals(copyID)) category = attributeCategoryService.cloneAttributeCategoryById(UUID.fromString(copyID)); 
		  else category = new AttributeCategory();
 	      model.addAttribute("attributeCategory", category);
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/attributeCategoryEdit :: editCategoryForm";
            }
          return "/admin/catalog/attributeCategoryEdit";		  
		  }	  

	  @RequestMapping("/attribute/add")
	  public String attributeAdd(@RequestParam(value = "ID", required = false) String copyID, Model model,
				 @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws CloneNotSupportedException {
		  AttributeForm attributeForm = null;
		  AbstractAttribute attribute = null;
		  if (copyID != null && !"".equals(copyID)) {
			  attribute = attributeCategoryService.cloneAttributeById(UUID.fromString(copyID));
			  if (attribute != null) attributeForm = new AttributeForm(attribute);  
		  }  
		  if(attributeForm == null) attributeForm = new AttributeForm();
		  model.addAttribute("attribute",attributeForm);
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/attributeEdit :: editAttributeForm";
            }
          return "/admin/catalog/attributeEdit";		  
	  }	  

	  
	  @RequestMapping("/attribute/edit")
	  public String attributeEdit(@RequestParam(value = "ID", required = false) String ID, Model model,
				 @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  if(!model.containsAttribute("attribute")) {
			 AttributeForm attribute = new AttributeForm(attributeCategoryService.getAttributeById(UUID.fromString(ID))); 			 
			 model.addAttribute("attribute", attribute);
		  }
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/attributeEdit :: editAttributeForm";
            }
          return "/admin/catalog/attributeEdit";		  
		  }	  
	  
	  @RequestMapping(value="/delete", method = RequestMethod.POST)
	  public String categoryDelete(@RequestParam(value = "ID", required = false) String ID,
			  					   @RequestParam(value = "phisycalDelete", required = false) Boolean phisycalDelete,
			  					 final RedirectAttributes ra) {
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  if (phisycalDelete != null && phisycalDelete)  {
			  AttributeCategory category = attributeCategoryService.loadAttributeCategoryById(UUID.fromString(ID), false);
			  if(category.canRemove() && authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
				  attributeCategoryService.deleteAttributeCategory(category);   
			  } else {
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", 
				            new String[] {messageSource.getMessage("admin.attribute.attribute", null, Locale.getDefault())} , null));

				  ra.addAttribute("error", true);
			  }
		  } else {
			  attributeCategoryService.softDeleteAttributeCategoryById(UUID.fromString(ID));
		  }
          return "redirect:/admin/catalog/attributecategory";		  
		  }	  

	  @RequestMapping(value="/attribute/delete", method = RequestMethod.POST)
	  public String attributeDelete(@RequestParam(value = "ID", required = false) String ID, 
			  						@RequestParam(value = "phisycalDelete", required = false) Boolean phisycalDelete,
			  						final RedirectAttributes ra) {
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  if (phisycalDelete != null && phisycalDelete)  { 
			  AbstractAttribute attribute = attributeCategoryService.getAttributeById(UUID.fromString(ID));
			  if (attribute.canRemove() && authorities.contains(new SimpleGrantedAuthority("ADMIN"))){
				  attributeCategoryService.deleteAttribute(attribute); 
			  } else {
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", 
				            new String[] {messageSource.getMessage("admin.attribute.attribute", null, Locale.getDefault())} , null));

				  ra.addAttribute("error", true);
			  }
		  } else {
			  attributeCategoryService.softDeleteAttributeById(UUID.fromString(ID));
		  }
          return "redirect:/admin/catalog/attributecategory";		  
		  }	  
	  

	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String saveCategory(
			                         @ModelAttribute @Valid AttributeCategory category,
			                         final BindingResult bindingResult,
			                         final RedirectAttributes ra
			                         ) {
          attributeCategoryService.mergeWithExistingAndUpdateOrCreate(category, bindingResult);
		   if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("attributeCategory", category);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.attributeCategory", bindingResult);
	            return "redirect:/admin/catalog/attributecategory/edit";
	        }
	        return "redirect:/admin/catalog/attributecategory";
	    }

	   @RequestMapping(value="/attribute/edit", method = RequestMethod.POST )
	   public String saveAttribute(
			                         @ModelAttribute("attribute") @Valid AttributeForm attribute, 
			                         final BindingResult bindingResult,
			                         final RedirectAttributes ra
			                         ) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
	       attributeCategoryService.mergeWithExistingAndUpdateOrCreate(attribute, bindingResult);
		   if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("attribute", attribute);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.attribute", bindingResult);
	            return "redirect:/admin/catalog/attributecategory/attribute/edit";
	        }
	        return "redirect:/admin/catalog/attributecategory";
	    }
	   
	   
}