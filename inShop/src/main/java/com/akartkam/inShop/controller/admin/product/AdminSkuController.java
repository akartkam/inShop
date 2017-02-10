package com.akartkam.inShop.controller.admin.product;


import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.akartkam.inShop.domain.product.InventoryType;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.attribute.AttributeType;
import com.akartkam.inShop.domain.product.attribute.SimpleAttributeFactory;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.akartkam.inShop.service.product.AttributeCategoryService;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.util.ImageUtil;
import com.akartkam.inShop.validator.SkuValidator;
import com.akartkam.inShop.exception.ProductNotFoundException;
import com.akartkam.inShop.exception.SkuNotFoundException;
import com.akartkam.inShop.formbean.SkuForm;

@Controller
@RequestMapping("/admin/catalog/sku")
public class AdminSkuController {
	  private static final Log LOG = LogFactory.getLog(AdminSkuController.class);
	  	
	  @Autowired
	  private ProductService productService;
	  
	  
	  @Autowired
	  private AttributeCategoryService attributeCategoryService;

	  @Autowired
	  private MessageSource messageSource;
	  
	  @Autowired(required=false)
	  private ImageUtil imageUtil;

	  @Autowired
	  private SkuValidator skuValidator;
	  
	  @Value("#{appProperties['inShop.imagesPath']}")
	  private String imagePath;
	  
	  @Value("#{appProperties['inShop.imagesUrl']}")
	  private String imageUrl;
	  	  	  	  
	  @ModelAttribute("allInventoryTypes")
	  public List<InventoryType> getAllInventoryTypes() {
	      return Arrays.asList(InventoryType.ALL);
	  }	  

	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "*id", "*name", "url", "*description", "*longDescription", "attributeValues*",
					 							   "*code", "ordering", "*images*", "enabled", "*retailPrice", "productOptionValuesList*", 
					 							   "*salePrice", "*costPrice", "*activeStartDate", "*activeEndDate", "productOptionsList*",
					 							   "quantityAvailable", "inventoryType", "quantityPerPackage"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });
			
			
			binder.registerCustomEditor(ProductOption.class,"productOptionsList", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		ProductOption po = productService.getPOByIdForForm(UUID.fromString(text));			    		
			            setValue(po);
			    	} else {
			    		setValue(null);
			    	}
			    }
			    });				
			
			binder.registerCustomEditor(ProductOptionValue.class,"productOptionValuesList", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		ProductOptionValue pov = productService.getPOVById(UUID.fromString(text));			    		
			            setValue(pov);
			    	} else {
			    		setValue(null);
			    	}
			    }
			    });
			
			binder.registerCustomEditor(AbstractAttributeValue.class,"attributeValues", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		String[] avv = text.split("_");
			    		AbstractAttributeValue<?> av = attributeCategoryService.getAttributeValueById(UUID.fromString(avv[1]));
			    		if (av == null)
							try {
								av = SimpleAttributeFactory.createAttributeValue(AttributeType.valueOf(avv[0]));
							} catch (ClassNotFoundException
									| InstantiationException
									| IllegalAccessException e) {
								// TODO Auto-generated catch block
								LOG.error(e);
							}
			            setValue(av);
			    	}
			    }
			    });	
			binder.registerCustomEditor(AbstractAttribute.class,"attributeValues.attribute", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		AbstractAttribute at = attributeCategoryService.getAttributeByIdForForm(UUID.fromString(text));			    		
			            setValue(at);
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
		  Sku sku = null;
		  Product product = null;
		  if(!model.containsAttribute("sku")) {
			  if ("".equals(ID)) {
				  LOG.error("ID Sku is empty.");
				  throw new SkuNotFoundException("ID Sku is empty.");
			  }
		      sku = productService.getSkuById(UUID.fromString(ID));
			  if (sku == null) {
				  LOG.error("Sku ID="+ID+" not found!");
				  throw new SkuNotFoundException("Sku ID="+ID+" not found!");
			  }		      
			  SkuForm skuForm = new SkuForm(sku);
			  skuForm.complementNecessaryAttributes();
		      model.addAttribute("sku", skuForm);
		      product = skuForm.getProduct();
		  }
		  if (product == null) {
			  sku = (Sku) model.asMap().get("sku");
			  product = productService.getProductById(sku.getProduct().getId());
		  }
	      model.addAttribute("product", product);		  
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
				  Product product = productService.getProductById(UUID.fromString(productID));
				  product.addAdditionalSku(sku);
			  }
		  }		  
 	      model.addAttribute("sku", new SkuForm(sku));
	      model.addAttribute("product", sku.getProduct()); 	      
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
		  Sku sku = productService.loadSkuById(UUID.fromString(ID), false);
		  if (phisycalDelete != null && phisycalDelete)  {
			  boolean hasPriv = authorities.contains(new SimpleGrantedAuthority("ADMIN")) || authorities.contains(new SimpleGrantedAuthority("MANAGER")); 
			  if(hasPriv && productService.canDelete(sku)) {
				  productService.deleteSku(sku);   
			  } else {
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", new String[] {"Can't remove the sku."} , Locale.getDefault()));
				  ra.addAttribute("error", true);
			  }

		  } else {
			  productService.softDeleteSkuById(UUID.fromString(ID));
		  }
          return "redirect:/admin/catalog/sku?productID="+sku.getProduct().getId();		  
		  }
	
	  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	  @RequestMapping(value="/batch-delete", method = RequestMethod.POST)
	  public String skuBatchDelete(@RequestParam(value = "IDS", required = true) List<String> IDS, 
			  					   @RequestParam(value = "productID", required = true) String productID,                     
			  					   final RedirectAttributes ra) {
		  boolean hasError = false;
		  for (String sid : IDS) {
			  UUID id = UUID.fromString(sid);
			  Sku sku = productService.loadSkuById(id, false);
			  if(productService.canDelete(sku)) {
				  productService.deleteSku(sku);   
			  } else if (!hasError) {
				  hasError = true;
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.batchDelete.message",null , Locale.getDefault()));
				  ra.addAttribute("error", true);
			  }			  
	      }
          return "redirect:/admin/catalog/sku?productID="+productID;		  
	}
	  	  
	  @RequestMapping(value="/edit", method = RequestMethod.POST )
	  public String saveProduct(
			                   @Valid SkuForm sku,
				   			   final BindingResult bindingResult,
			                   final RedirectAttributes ra
			                         ) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		   skuValidator.validate(sku, bindingResult);
		   productService.mergeWithExistingSkuAndUpdateOrCreate(sku, bindingResult);
		   if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("sku", sku);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.sku", bindingResult);
	            return "redirect:/admin/catalog/sku/edit?ID="+sku.getId();
	        }
	       
           return "redirect:/admin/catalog/sku?productID="+sku.getProduct().getId();
	    }
	   
	   
	   @RequestMapping(value="/image/add", method = RequestMethod.POST )
	   public String addNewImage(final @ModelAttribute SkuForm sku,
			   				   @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			   				   @RequestParam(value = "newImage", required = false)	MultipartFile image,
			   				   final Model model,
			   				   final BindingResult bindingResult
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
