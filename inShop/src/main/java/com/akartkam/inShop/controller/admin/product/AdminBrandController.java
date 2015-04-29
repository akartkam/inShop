package com.akartkam.inShop.controller.admin.product;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.service.product.BrandService;

@Controller
@RequestMapping("/admin/catalog/brand")
public class AdminBrandController {
	
	  @Autowired
	  BrandService brandService;
	  
	  @SuppressWarnings("rawtypes")
	  @ModelAttribute("allBrand")
	  public List getAllBrand() {
	      return brandService.getAllBrand();
	  }
	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "name", "url", "logoUrl", "enabled"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });
			
	  }
	  
	  @RequestMapping(method=GET)
	  public String category(Model model) {
		  return "/admin/catalog/brand"; 
		  }	  

}
