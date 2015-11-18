package com.akartkam.inShop.controller.admin.product;


import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.ProductStatus;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.attribute.AttributeDecimalValue;
import com.akartkam.inShop.domain.product.attribute.AttributeType;
import com.akartkam.inShop.domain.product.attribute.SimpleAttributeFactory;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.akartkam.inShop.service.product.AttributeCategoryService;
import com.akartkam.inShop.service.product.BrandService;
import com.akartkam.inShop.service.product.CategoryService;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.util.ImageUtil;
import com.akartkam.inShop.util.NullAwareBeanUtilsBean;
import com.akartkam.inShop.exception.ImageUploadException;
import com.akartkam.inShop.exception.ProductNotFoundException;
import com.akartkam.inShop.formbean.ProductForm;
import com.akartkam.inShop.domain.product.attribute.Selectable;

@Controller
@RequestMapping("/admin/catalog/sku")
public class AdminSkuController {
	  private static final Log LOG = LogFactory.getLog(AdminSkuController.class);
	  	
	  @Autowired
	  ProductService productService;
	  
	  
	  @Autowired
	  AttributeCategoryService attributeCategoryService;

	  @Autowired
	  private MessageSource messageSource;
	  
	  @Autowired(required=false)
	  private ImageUtil imageUtil;

	  
	  @Value("#{appProperties['inShop.imagesPath']}")
	  private String imagePath;
	  
	  @Value("#{appProperties['inShop.imagesUrl']}")
	  private String imageUrl;
	  	  	  	  
	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "*id", "*name", "url", "*description", "*longDescription", 
					 							   "*code", "ordering", "*images*", "enabled", "*retailPrice", 
					 							   "*salePrice", "*costPrice", "*activeStartDate", "*activeEndDate"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });
			
			
			PropertyEditor pe = new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		SimpleDateFormat formatter = new SimpleDateFormat(messageSource.getMessage("date.format", null, Locale.getDefault()));
			    		try {
			    			Date date = formatter.parse(text);
				            setValue(date);
			    		} catch (ParseException e) {
			    			LOG.error(e);
			    		}			    		

			    	} else {
			    		setValue(null);
			    	}
			    }
			    };

			binder.registerCustomEditor(java.util.Date.class,"activeStartDate", pe);
			binder.registerCustomEditor(java.util.Date.class,"activeEndDate", pe);
			
			binder.registerCustomEditor(ProductOptionValue.class,"productOptionValuesList", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		ProductOptionValue pov = productService.loadPOVById(UUID.fromString(text), false);			    		
			            setValue(pov);
			    	}			    
			    }
			    });			
	  
	  }

	  
	  @RequestMapping(method=GET)
	  public String sku(@RequestParam(value = "productID") String productID, Model model) {
		  if ("".equals(productID)) throw new ProductNotFoundException("ID Product is empty.");
	      Product product = productService.getProductById(UUID.fromString(productID)); 
	      model.addAttribute("product", product);
		  return "/admin/catalog/sku"; 
		  }	  
	  
	  
	  @RequestMapping("/edit")
	  public String productEdit(@RequestParam(value = "ID", required = false) String ID, Model model,
			   				    @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {

		  
		  if(!model.containsAttribute("sku")) {
			  if ("".equals(ID)) {
				  LOG.error("ID Sku is empty.");
				  throw new ProductNotFoundException("ID Sku is empty.");
			  }
		      Sku sku = productService.getSkuById(UUID.fromString(ID)); 
		      model.addAttribute("sku", sku);
		  }
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/skuEdit :: editSkuForm";
            }		  
          return "/admin/catalog/skuEdit";		  
	   }	  
	  
	  @RequestMapping("/add")
	  public String productAdd(@RequestParam(value = "ID", required = false) String copyID,
			  				   @RequestParam(value = "productID", required = false) String productID, Model model,
				               @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws CloneNotSupportedException {
		  Sku sku = null;
		  if (copyID != null && !"".equals(copyID)) {
			  sku = productService.cloneSkuById(UUID.fromString(copyID)); 
		  }
		  if (sku == null) {
			  sku = new Sku();
			  if (productID !=null && !"".equals(productID)) {
				  Product product = productService.loadProductById(UUID.fromString(productID), false);
				  product.addAdditionalSku(sku);
			  }
		  }		  
 	      model.addAttribute("sku", sku);
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/skuEdit :: editSkuForm";
            } 	      
          return "/admin/catalog/skuEdit";		  
		  }		  

	  @RequestMapping(value="/delete", method = RequestMethod.POST)
	  public String productDelete(@RequestParam(value = "ID", required = false) String ID, 
			                       @RequestParam(value = "phisycalDelete", required = false) Boolean phisycalDelete,
				                   final RedirectAttributes ra) {
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  if (phisycalDelete != null && phisycalDelete)  {
			  Product product = productService.loadProductById(UUID.fromString(ID), false);
			  if(product.canRemove() && authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
				  productService.deleteProduct(product);   
			  } else {
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", new String[] {"�����"} , Locale.getDefault()));
				  ra.addAttribute("error", true);
			  }

		  } else {
			  productService.softDeleteProductById(UUID.fromString(ID));
		  }
          return "redirect:/admin/catalog/product";		  
		  }
	
	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String saveProduct(
			                   @Valid Sku sku,
				   			   final BindingResult bindingResult,
			                   final RedirectAttributes ra
			                         ) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		   if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("sku", sku);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.sku", bindingResult);
	            return "redirect:/admin/catalog/sku/edit?ID="+sku.getId();
	        }

	        //productService.mergeWithExistingAndUpdateOrCreate(product);	       
	        return "redirect:/admin/catalog/sku";
	    }
	   
	   
	   @RequestMapping(value="/image/add", method = RequestMethod.POST )
	   public String addNewImage(final @ModelAttribute Sku sku,
			   				   @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			   				   @RequestParam(value = "newImage", required = false)	MultipartFile image,
			                   final BindingResult bindingResult,
			                   final Model model
			                         ) throws CloneNotSupportedException {
		   if (!"XMLHttpRequest".equals(requestedWith)) throw new IllegalStateException("The addNewImage method can be called only via ajax!");
	       String fileName="";
	       String filePath="";
	       imageUtil.validateImage(image, "images", bindingResult);
	       if(!bindingResult.hasErrors()) {
		        fileName = new File(image.getOriginalFilename()).getName(); 
		        filePath = imagePath + fileName;
	        	imageUtil.saveImage(filePath, image);	
	        	sku.getImages().add(imageUrl+fileName);
	       }
		   model.addAttribute("sku", sku);
		   model.addAttribute("tabactive","images");
	       return "/admin/catalog/skuEdit :: imageTable";
	    }

	   @RequestMapping(value="/gen")
	   public String genSkus(  @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			   				   @RequestParam(value = "productID", required=true) String productID,
			                   final Model model) {
		   if (!"XMLHttpRequest".equals(requestedWith)) throw new IllegalStateException("The genSkus method can be called only via ajax!");
           Product product =  productService.generateSkusFromProduct(UUID.fromString(productID));
		   model.addAttribute("product", product);
	       return "/admin/catalog/sku :: skuDataTable";
	    }
	   
	   
}
