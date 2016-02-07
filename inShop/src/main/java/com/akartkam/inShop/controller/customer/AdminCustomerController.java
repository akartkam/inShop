package com.akartkam.inShop.controller.customer;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;

import java.util.List;
import java.util.UUID;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.akartkam.inShop.domain.customer.Customer;
import com.akartkam.inShop.service.customer.CustomerService;

@Controller
@RequestMapping("/admin/customer/customer")
public class AdminCustomerController {
	
	  @Autowired
	  CustomerService customerService;
	  
	  @Autowired
	  private MessageSource messageSource;
	  
 
	  @ModelAttribute("allCustomer")
	  public List<Customer> getAllCustomer() {
	      return customerService.getAllCustomer();
	  }
	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "firstName", "lastName", "middleName", "email",
					                               "phone", "address", "birthdate"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });						
			
	  }
	  
	  @RequestMapping(method=GET)
	  public String customer() {
		  return "/admin/customer/customer"; 
	  }	  
	 /* 
	  @RequestMapping("/edit")
	  public String brandEdit(@RequestParam(value = "ID", required = false) String categoryID, Model model,
			   				  @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  if(!model.containsAttribute("brand")) {
			 Brand brand = brandService.getBrandById(UUID.fromString(categoryID));
		     model.addAttribute("brand", brand);
		  }
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/brandEdit :: editBrandForm";
            }		  
          return "/admin/catalog/brandEdit";		  
		  }	  
	  
	  @RequestMapping("/add")
	  public String brandAdd(@RequestParam(value = "ID", required = false) String copyID, Model model,
				                @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws CloneNotSupportedException {
		  Brand brand = null;
		  if (copyID != null && !"".equals(copyID)) brand = brandService.cloneBrandById(UUID.fromString(copyID)); 
		  else brand = new Brand();
 	      model.addAttribute("brand", brand);
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/brandEdit :: editBrandForm";
            } 	      
          return "/admin/catalog/brandEdit";		  
		  }		  

	  @RequestMapping(value="/delete", method = RequestMethod.POST)
	  public String brandDelete(@RequestParam(value = "ID", required = false) String ID, 
			                       @RequestParam(value = "phisycalDelete", required = false) Boolean phisycalDelete,
				                   final RedirectAttributes ra) {
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  if (phisycalDelete != null && phisycalDelete)  {
			  Brand brand = brandService.loadBrandById(UUID.fromString(ID), false);
			  if(brand.canRemove() && authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
				  brandService.deleteBrand(brand);   
			  } else {
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", new String[] {"�����"} , null));
				  ra.addAttribute("error", true);
			  }

		  } else {
			  brandService.softDeleteBrandById(UUID.fromString(ID));
		  }
          return "redirect:/admin/catalog/brand";		  
		  }
	  
	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String saveBrand(@ModelAttribute @Valid Brand brand,
			                   final BindingResult bindingResult,
			                   @RequestParam(value = "mainLogo", required = false)	MultipartFile image,
			                   final RedirectAttributes ra
			                         ) {
	        if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("brand", brand);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.brand", bindingResult);
	            return "redirect:/admin/catalog/brand/edit";
	        }
	        String fileName="";
	        String filePath="";
	        imageUtil.validateImage(image, "logoUrl", bindingResult);
	        if(!bindingResult.hasErrors()) {
		        fileName = new File(image.getOriginalFilename()).getName();
		        filePath = imagePath + fileName;
	        	imageUtil.saveImage(filePath, image);		        
		        brand.setLogoUrl(imageUrl+fileName);
	        }
	        
 
	        brandService.mergeWithExistingAndUpdateOrCreate(brand);	        
	        
	        return "redirect:/admin/catalog/brand";
	    }
	*/   

}
