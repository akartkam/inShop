package com.akartkam.inShop.controller.admin.product;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.List;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.akartkam.inShop.service.product.ProductService;

@Controller
@RequestMapping("/admin/catalog/po")
public class AdminProductOptionController {
	
	  @Autowired
	  private ProductService productService;
	  
	  //@Autowired
	  //private ProductOptionValidator bigDecimalValidator;
	  
	  @Autowired
	  private MessageSource messageSource;
	  
	  

	  @ModelAttribute("allPo")
	  public List<ProductOption> getAllPO() {
	      return productService.getAllPO();
	  }
	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "*id", "name", "label", "required", "useInSkuGeneration", "*optionValue",
												   "*priceAdjustment", "*ordering", "*enabled"});
			
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });			

	  }
	  
	  @RequestMapping(method=GET)
	  public String po() {
		  return "/admin/catalog/po"; 
		  }	  
	  
	  @RequestMapping("/edit")
	  public String poEdit(@RequestParam(value = "ID", required = false) String ID, Model model,
			   				  @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  if(!model.containsAttribute("po")) {
			 ProductOption po = productService.getPOById(UUID.fromString(ID));
		     model.addAttribute("po", po);
			 //model.addAttribute("tabactive","main");
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
		  //model.addAttribute("tabactive","main");
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
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", new String[] {"�����"} , null));
				  ra.addAttribute("error", true);
			  }

		  } else {
			  productService.softDeletePOById(UUID.fromString(ID));
		  }
          return "redirect:/admin/catalog/po";		  
		  }
	  
	  @RequestMapping(value="/pov/delete", method = RequestMethod.POST)
	  public String povDelete(@RequestParam(value = "idpov", required = false) String ID, 
			                       @RequestParam(value = "phisycalDelete", required = false) Boolean phisycalDelete,
			                       @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			                       @ModelAttribute ProductOption po,
				                   Model model) {
		  if (!"XMLHttpRequest".equals(requestedWith)) throw new IllegalStateException("The povDelete method can be called only via ajax!");
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  ProductOptionValue pov = po.getProductOptionValueById(UUID.fromString(ID));
		  if (phisycalDelete != null && phisycalDelete)  {
			  if(pov.canRemove() && authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
				  po.getProductOptionValues().remove(pov);   
			  } 
		  } else {
			  pov.setEnabled(false);
		  }
		  model.addAttribute("po", po);
		  model.addAttribute("tabactive","content");
          return "/admin/catalog/poEdit :: editPOForm";
		  }
	  
	  @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String savePO(@ModelAttribute @Valid ProductOption po,
			                   final BindingResult bindingResult,
			                   final RedirectAttributes ra
			                         ) {
	      productService.mergeWithExistingPOAndUpdateOrCreate(po, bindingResult);
		  if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("po", po);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.po", bindingResult);
	            return "redirect:/admin/catalog/po/edit";
	        }
	        return "redirect:/admin/catalog/po";
	    }

	   @RequestMapping(value="/pov/add", method = RequestMethod.POST )
	   public String addNewPov(
			   				   final @RequestParam(value = "copyid", required = false) String ID,
			   				         @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			   				   final @ModelAttribute("po") @Valid ProductOption po,
			                   final BindingResult bindingResult,
			                   final Model model
			                         ) throws CloneNotSupportedException {
		   if (!"XMLHttpRequest".equals(requestedWith)) throw new IllegalStateException("The povDelete method can be called only via ajax!");
		   ProductOptionValue pov=null;
		   if (ID != null && !"".equals(ID) && !"new".equals(ID)) {
			   ProductOptionValue povCopy = po.getProductOptionValueById(UUID.fromString(ID)); 
			   if (povCopy != null) {
				   pov = (ProductOptionValue) povCopy.clone(); 
			   }
		   }
		   if (pov==null) pov=new ProductOptionValue();
		   pov.setProductOption(po);
		   po.getProductOptionValues().add(pov);
		   model.addAttribute("po", po);
		   model.addAttribute("tabactive","content");
	       return "/admin/catalog/poEdit :: editPOForm";
	    }	   

}

