package com.akartkam.inShop.controller.admin.product;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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

import com.akartkam.inShop.domain.Instruction;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;
import com.akartkam.inShop.exception.CategoryNotFoundException;
import com.akartkam.inShop.exception.ProductNotFoundException;
import com.akartkam.inShop.formbean.CategoryForm;
import com.akartkam.inShop.service.InstructionService;
import com.akartkam.inShop.service.product.AttributeCategoryService;
import com.akartkam.inShop.service.product.CategoryService;
import com.akartkam.inShop.validator.CategoryValidator;

@Controller
@RequestMapping("/admin/catalog/category")
public class AdminCategoryController {
	

	  @Autowired
	  private CategoryService categoryService;
	  
	  @Autowired
	  private MessageSource messageSource;
	  
	  @Autowired
	  private AttributeCategoryService attributeCategoryService;
	  
	  @Autowired
	  private CategoryValidator categoryValidator;
	  
	  @Autowired
	  private InstructionService instructionService;
	  
	  
	  @ModelAttribute("allCategories")
	  public List<Category> getAllCategories() {
		  List<Category> cats = categoryService.getAllCategoryHierarchy(true);
		  for (Category cat : cats) {
			 cat.tag = StringUtils.repeat("|&nbsp;&nbsp;&nbsp;&nbsp;", cat.getDepthNesting()); 
			 cat.tag = new StringBuilder(cat.tag).append(" ").append(cat.getName().trim()).toString();
		  }
		  return cats; 
	  }	  
	  
	  @ModelAttribute("allAttributeCategories")
	  public List<AttributeCategory> getAllAttributeCategoriesHierarchy() {
	      return attributeCategoryService.getAllAttributeCategory();
	  }
 
	  @ModelAttribute("allAttributes")
	  public List<AbstractAttribute> getAllAttributes(@RequestParam(value = "categoryID", required = false) String categoryID) {
		  List<AbstractAttribute> al = new ArrayList<AbstractAttribute>(0);
          if (categoryID == null || "".equals(categoryID)) return al;
		  Category category = categoryService.getCategoryById(UUID.fromString(categoryID));
    	  if (category != null) al = category.getAllAttributes(null, true);
	      return al;
	  }
	 
	  @ModelAttribute("selfAttributes")
	  public List<AbstractAttribute> getSelfAttributes(@RequestParam(value = "categoryID", required = false) String categoryID) {
		  List<AbstractAttribute> al = new ArrayList<AbstractAttribute>(0);
          if (categoryID == null || "".equals(categoryID)) return al;
		  Category category = categoryService.getCategoryById(UUID.fromString(categoryID));
    	  if (category != null) al = new ArrayList<AbstractAttribute>(category.getAttributes()); 
	      return al;
	  }
	  
	  @ModelAttribute("allInstructions")
	  public List<Instruction> getAllInstructions() {
		return instructionService.getAllInstructions();
	  }	  
	 
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "name", "parent", "urlForForm", "showQuanPerPackOnProductHeader", 
					"description", "longDescription", "ordering", "enabled", "*attributesForForm*", "instruction",
					"h1", "metaTitle", "metaDescription", "metaKeywords"});
			binder.registerCustomEditor(Category.class, "parent", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		Category ch = categoryService.getCategoryById(UUID.fromString(text));
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
			binder.registerCustomEditor(AbstractAttribute.class, "attributesForForm", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		AbstractAttribute at = attributeCategoryService.loadAttributeById(UUID.fromString(text), false);
			            setValue(at);
			    	} else {
			    		setValue(null);
			    	}
			    }
			});	
			binder.registerCustomEditor(Instruction.class, "instruction",new PropertyEditorSupport(){
				@Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		Instruction instr = instructionService.getInstructionById(UUID.fromString(text));
			            setValue(instr);
			    	} else {
			    		setValue(null);
			    	}
			    }				
			});
	  }
	
	  
	  @RequestMapping(method=GET)
	  public String category() {
		  return "/admin/catalog/category"; 
		  }	  
	  
	   
	  @RequestMapping("/edit")
	  public String categoryEdit(@RequestParam(value = "categoryID", required = true) String categoryID, Model model,
			   					 @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  if(!model.containsAttribute("category")) {
			 Category category = categoryService.getCategoryById(UUID.fromString(categoryID));
			 if (category == null) throw new CategoryNotFoundException("Category with ID="+categoryID+" not found");
		     model.addAttribute("category", new CategoryForm(category));
		  }
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/categoryEdit :: editCategoryForm";
            }		  
          return "/admin/catalog/categoryEdit";		  
	  }	  

	  @RequestMapping("/add")
	  public String categoryAdd(@RequestParam(value = "categoryID", required = false) String copyCategoryID, Model model,
				                @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws CloneNotSupportedException {
		  Category category = null;
		  CategoryForm categoryForm = null;
		  if (copyCategoryID != null && !"".equals(copyCategoryID)) {
			  category = categoryService.cloneCategoryById(UUID.fromString(copyCategoryID));
			  if (category != null) categoryForm = new CategoryForm(category);  
		  }
		  if (categoryForm == null) categoryForm = new CategoryForm();
 	      model.addAttribute("category", categoryForm);
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
			  Category category = categoryService.loadCategoryById(UUID.fromString(categoryID), false);
			  if(category.canRemove() && authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
				  categoryService.deleteCategory(category);   
			  } else {
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", 
						            new String[] {messageSource.getMessage("admin.catalog.category", null, Locale.getDefault())} , null));
				  ra.addAttribute("error", true);
			  }

		  } else {
		      categoryService.softDeleteCategoryById(UUID.fromString(categoryID));
		  }
          return "redirect:/admin/catalog/category";		  
		  }	  
	  

	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String saveCategory(   @ModelAttribute @Valid CategoryForm category,
			                         final BindingResult bindingResult,
			                         final RedirectAttributes ra
			                         ) {
		   categoryValidator.validate(category, bindingResult);
	       if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("category", category);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.category", bindingResult);
	            return "redirect:/admin/catalog/category/edit?categoryID="+category.getId();
	       }
	       categoryService.mergeWithExistingAndUpdateOrCreate(category);
	       return "redirect:/admin/catalog/category";
	    }
	  
}