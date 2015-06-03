package com.akartkam.inShop.controller.admin.product;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.service.product.BrandService;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.exception.ImageUploadException;

@Controller
@RequestMapping("/admin/catalog/po")
public class ProductOptionController {
	
	  @Autowired
	  ProductService productService;
	  	  
	  @SuppressWarnings("rawtypes")
	  @ModelAttribute("allPo")
	  public List getAllPO() {
	      return productService.getAllPO();
	  }
	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "name", "label", "required", "useInSkuGeneration", "enabled"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });			
	  }
	  
	  @RequestMapping(method=GET)
	  public String po(Model model) {
		  return "/admin/catalog/po"; 
		  }	  
	  
	  @RequestMapping("/edit")
	  public String poEdit(@RequestParam(value = "ID", required = false) String categoryID, Model model,
			   				  @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  if(!model.containsAttribute("po")) {
			 ProductOption po = productService.getPOById(UUID.fromString(categoryID));
		     model.addAttribute("po", po);
		  }
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/poEdit :: editPOForm";
            }		  
          return "/admin/catalog/poEdit";		  
		  }	  
	  
	  @RequestMapping("/add")
	  public String poAdd(@RequestParam(value = "ID", required = false) String copyID, Model model,
				                @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws CloneNotSupportedException {
		  ProductOption po = null;
		  if (copyID != null && !"".equals(copyID)) po = productService.clonePOById(UUID.fromString(copyID)); 
		  else po = new ProductOption();
 	      model.addAttribute("po", po);
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/poEdit :: editPOForm";
            } 	      
          return "/admin/catalog/poEdit";		  
		  }		  

	  @RequestMapping(value="/delete", method = RequestMethod.POST)
	  public String poDelete(@RequestParam(value = "ID", required = false) String ID, 
			                       @RequestParam(value = "phisycalDelete", required = false) Boolean phisycalDelete,
				                   final RedirectAttributes ra) {
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  if (phisycalDelete != null && phisycalDelete)  {
			  ProductOption po = productService.loadPOById(UUID.fromString(ID), false);
			  if(po.canRemove() && authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
				  productService.deletePO(po);   
			  } else {
				  ra.addFlashAttribute("errormessage", "Нельзя удалить опцию. Имеются ссылки на другие сущности, либо недостаточно прав.");
				  ra.addAttribute("error", true);
			  }

		  } else {
			  productService.softDeletePOById(UUID.fromString(ID));
		  }
          return "redirect:/admin/catalog/po";		  
		  }
	  
	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String savePO(final @Valid ProductOption po,
			                   final BindingResult bindingResult,
			                   final RedirectAttributes ra
			                         ) {
	        if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("po", po);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.po", bindingResult);
	            return "redirect:/admin/catalog/po/edit";
	        }
	        productService.mergeWithExistingPOAndUpdateOrCreate(po);
	       	        
	        return "redirect:/admin/catalog/po";
	    }
	   

}
