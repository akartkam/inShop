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

import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.InventoryType;
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
import com.akartkam.inShop.service.order.OrderService;
import com.akartkam.inShop.service.product.AttributeCategoryService;
import com.akartkam.inShop.service.product.BrandService;
import com.akartkam.inShop.service.product.CategoryService;
import com.akartkam.inShop.service.product.ProductService;
import com.akartkam.inShop.util.ImageUtil;
import com.akartkam.inShop.util.NullAwareBeanUtilsBean;
import com.akartkam.inShop.validator.SkuValidator;
import com.akartkam.inShop.exception.ImageUploadException;
import com.akartkam.inShop.exception.ProductNotFoundException;
import com.akartkam.inShop.formbean.ProductForm;
import com.akartkam.inShop.domain.product.attribute.Selectable;

@Controller
@RequestMapping("/admin/catalog/product")
public class AdminProductController {
	  private static final Log LOG = LogFactory.getLog(AdminProductController.class);
	  	
	  @Autowired
	  private ProductService productService;
	  
	  @Autowired
	  private CategoryService categoryService;
	  
	  @Autowired
	  private BrandService brandService;
	   
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
	  
	  
	  @ModelAttribute("allProduct")
	  public List<Product> getAllProduct() {
	      return productService.getAllProduct();
	  }
	  
	  @ModelAttribute("allCategory")
	  public List<Category> getAllCategory() {
	      return categoryService.getAllCategoryHierarchy(true);
	  }	  
	  
	  
	  @ModelAttribute("allBrand")
	  public List<Brand> getAllBrand() {
	      return brandService.getAllBrand(false);
	  }	
	  
	  @ModelAttribute("allPo")
	  public List<ProductOption> getAllPO() {
	      return productService.getAllPO();
	  }	  
	  
	  @ModelAttribute("allProdStatus")
	  public List<ProductStatus> getAllProdStatus() {
	      return Arrays.asList(ProductStatus.ALL);
	  }	  
	  
	  @ModelAttribute("allInventoryTypes")
	  public List<InventoryType> getAllInventoryTypes() {
	      return Arrays.asList(InventoryType.ALL);
	  }	  
  	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "*id", "*name", "urlForForm", "*description", "*longDescription", 
					 							   "*code", "category", "brand", "*model", "attributeValues*", "ordering", 
					 							   "*productOptions", "canSellWithoutOptions", "*images*", "enabled",
					 							   "*retailPrice", "*salePrice", "*costPrice", "*value", "*productStatus*", 
					 							   "*productOptionsForForm*","*activeStartDate", "*activeEndDate", "*quantityAvailable",
					 							   "*inventoryType"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });
			
			binder.registerCustomEditor(Category.class, "category", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		Category ch = categoryService.loadCategoryById(UUID.fromString(text), false);
			            setValue(ch);
			    	}			    
			    }
			    });

			binder.registerCustomEditor(Brand.class, "brand", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		Brand ch = brandService.loadBrandById(UUID.fromString(text), false);
			            setValue(ch);
			    	}			    
			    }
			    });
			binder.registerCustomEditor(ProductStatus.class,"productStatus", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		ProductStatus p = ProductStatus.forName(text); 
			            setValue(p);
			    	}			    
			    }
			    });	
			binder.registerCustomEditor(ProductOption.class,"productOptionsForForm", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if (!"".equals(text)) {
			    		ProductOption p = productService.loadPOById(UUID.fromString(text), false); 
			            setValue(p);
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
			/*
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

			binder.registerCustomEditor(java.util.Date.class,"defaultSku.activeStartDate", pe);
			binder.registerCustomEditor(java.util.Date.class,"defaultSku.activeEndDate", pe);
	  */
	  }

	  
	  @RequestMapping(method=GET)
	  public String product() {
		  return "/admin/catalog/product"; 
		  }	  
	  
	  
	  @RequestMapping("/edit")
	  public String productEdit(@RequestParam(value = "ID", required = false) String ID, Model model,
			   				    @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
		  if(!model.containsAttribute("product")) {
			  if ("".equals(ID)) throw new ProductNotFoundException("ID Product is empty.");
		      Product product = productService.getProductById(UUID.fromString(ID)); 
		      ProductForm productForm = new ProductForm(product);
		      productForm.complementNecessaryAttributes();
		      model.addAttribute("product", productForm);
		  }
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/productEdit :: editProductForm";
            }		  
          return "/admin/catalog/productEdit";		  
	   }	  
	  
	  @RequestMapping("/add")
	  public String productAdd(@RequestParam(value = "ID", required = false) String copyID, Model model,
				                @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws CloneNotSupportedException {
		  Product product = null;
		  ProductForm productForm = null;
		  if (copyID != null && !"".equals(copyID)) {
			  product = productService.cloneProductById(UUID.fromString(copyID));
			  if (product != null) productForm = new ProductForm(product); 
		  }
		  if (productForm == null) {
			  productForm = new ProductForm();
			  productForm.setDefaultSku(new Sku());
			  
		  }		  
 	      model.addAttribute("product", productForm);
          if ("XMLHttpRequest".equals(requestedWith)) {
              return "/admin/catalog/productEdit :: editProductForm";
            } 	      
          return "/admin/catalog/productEdit";		  
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
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", 
						  new String[] {messageSource.getMessage("admin.catalog.product", null, Locale.getDefault())} , Locale.getDefault()));
				  ra.addAttribute("error", true);
			  }

		  } else {
			  productService.softDeleteProductById(UUID.fromString(ID));
		  }
          return "redirect:/admin/catalog/product";		  
		  }
	
	   @RequestMapping(value="/edit", method = RequestMethod.POST )
	   public String saveProduct(
			                   @Valid ProductForm product,
				   			   final BindingResult bindingResult,
			                   final RedirectAttributes ra
			                         ) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		   skuValidator.validate(product.getDefaultSku(), bindingResult);
	       productService.mergeWithExistingAndUpdateOrCreate(product, bindingResult);	       
		   if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("product", product);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.product", bindingResult);
	            return "redirect:/admin/catalog/product/edit?ID="+product.getId();
	        }

	        return "redirect:/admin/catalog/product";
	    }
	   
	   
	   @RequestMapping(value="/image/add", method = RequestMethod.POST )
	   public String addNewImage(final @ModelAttribute ProductForm product,
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
	        	product.getDefaultSku().getImages().add(imageUrl+fileName);
	       }
		   model.addAttribute("product", product);
		   model.addAttribute("tabactive","images");
	       return "/admin/catalog/productEdit :: imageTable";
	    }
	      
}
