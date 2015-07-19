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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.service.product.BrandService;
import com.akartkam.inShop.service.product.CategoryService;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.exception.ImageUploadException;

@Controller
@RequestMapping("/admin/catalog/product")
public class AdminProductController {
	
	  @Autowired
	  ProductService productService;
	  
	  @Autowired
	  CategoryService categoryService;
	  
	  @Autowired
	  BrandService brandService;	  

	  @Autowired
	  private MessageSource messageSource;

	  
	  @Value("#{appProperties['inShop.imagesPath']}")
	  private String imagePath;
	  
	  @SuppressWarnings("rawtypes")
	  @ModelAttribute("allProduct")
	  public List getAllProduct() {
	      return productService.getAllProduct();
	  }
	  
	  @SuppressWarnings("rawtypes")
	  @ModelAttribute("allCategory")
	  public List getAllCategory() {
	      return categoryService.getAllCategoryHierarchy(true);
	  }	  
	  
	  @SuppressWarnings("rawtypes")
	  @ModelAttribute("allBrand")
	  public List getAllBrand() {
	      return brandService.getAllBrand(false);
	  }	
	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "name", "url", "description", "longDescription", 
					 							   "code", "category", "brand", "model", "attributeValues", 
					 							   "productOptions", "canSellWithoutOptions", "images", "enabled",
					 							   "retailPrice", "salePrice", "costPrice"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });
			
	  }
	  
	  @RequestMapping(method=GET)
	  public String product() {
		  return "/admin/catalog/product"; 
		  }	  
	  
	  @RequestMapping("/edit")
	  public String brandEdit(@RequestParam(value = "ID", required = false) String ID, Model model,
			   				  @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		  if(!model.containsAttribute("product")) {
			 Product product = productService.getProductById(UUID.fromString(ID));
		     model.addAttribute("product", product);
		  }
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/productEdit :: editProductForm";
            }		  
          return "/admin/catalog/productEdit";		  
		  }	  
	  
	  @RequestMapping("/add")
	  public String brandAdd(@RequestParam(value = "ID", required = false) String copyID, Model model,
				                @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws CloneNotSupportedException {
		  Product product = null;
		  if (copyID != null && !"".equals(copyID)) product = productService.cloneProductById(UUID.fromString(copyID)); 
		  else product = new Product();
 	      model.addAttribute("product", product);
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/productEdit :: editProductForm";
            } 	      
          return "/admin/catalog/productEdit";		  
		  }		  

	  @RequestMapping(value="/delete", method = RequestMethod.POST)
	  public String brandDelete(@RequestParam(value = "ID", required = false) String ID, 
			                       @RequestParam(value = "phisycalDelete", required = false) Boolean phisycalDelete,
				                   final RedirectAttributes ra) {
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  if (phisycalDelete != null && phisycalDelete)  {
			  Product product = productService.loadProductById(UUID.fromString(ID), false);
			  if(product.canRemove() && authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
				  productService.deleteProduct(product);   
			  } else {
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", new String[] {"�����"} , null));
				  ra.addAttribute("error", true);
			  }

		  } else {
			  productService.softDeleteProductById(UUID.fromString(ID));
		  }
          return "redirect:/admin/catalog/product";		  
		  }
	
	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String saveBrand(@ModelAttribute @Valid Product product,
			                   final BindingResult bindingResult,
			                   final RedirectAttributes ra
			                         ) {
	        if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("product", product);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.product", bindingResult);
	            return "redirect:/admin/catalog/product/edit";
	        }

	        productService.mergeWithExistingAndUpdateOrCreate(product);
	       
	        return "redirect:/admin/catalog/product";
	    }
	   
	   /*
		private void validateImage(MultipartFile image) {
			String allowedFileType = "image/jpeg,image/png,image/gif";
			if (allowedFileType.indexOf(image.getContentType()) < 0) {
				throw new ImageUploadException("Only jpeg, png, gif images accepted");
			}
		}
		
		private void saveImage(String filePath, MultipartFile image)
				throws ImageUploadException {
			try {
				
				File file = new File(filePath);
				FileUtils.writeByteArrayToFile(file, image.getBytes());
			} catch (IOException e) {
				throw new ImageUploadException("Unable to save image", e);
			}
		}	   
*/
}
