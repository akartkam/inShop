package com.akartkam.inShop.controller.admin.order;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.akartkam.inShop.domain.order.Delivery;
import com.akartkam.inShop.domain.order.Store;
import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.service.order.DeliveryService;
import com.akartkam.inShop.util.ImageUtil;

@Controller
@RequestMapping("/admin/delivery")
public class AdminDeliveryController {
	
	@Autowired
	private DeliveryService deliveryService; 
	
	@Autowired(required=false)
	private ImageUtil imageUtil;	
	
	@Value("#{appProperties['inShop.imagesPath']}")
    private String imagePath;

	@Value("#{appProperties['inShop.imagesUrl']}")
	private String imageUrl;	
	
	@ModelAttribute("allStores")
	public List<Store> getAllStores() {
	      return deliveryService.getAllStores();
	}
	
	@ModelAttribute("allDeliverys")
	public List<Delivery> getAllDeliverys() {
	      return deliveryService.getAllDeliverys();
	}

	
	@RequestMapping(value="/store", method=GET)
	public String stores() {
		  return "/admin/delivery/store"; 
	}
	
	@RequestMapping(value="/delivery", method=GET)
	public String deliverys() {
		  return "/admin/delivery/delivery"; 
	}

	
	@InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "name", "enabled", "longDescription","address", "phone", "imageUrl",
												   "workSchedule", "mapScript", "ordering", "deliveryType", "stores",
												   "isPublic"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });
			binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if ("".equals(text) || "''".equals(text))
			    	  setValue(null);
			    	else
			    	  setValue(text);	
			    }
			    });
	  }	
	  
    @RequestMapping("/store/edit")
    public String storeEdit(@RequestParam(value = "ID", required = false) String storeID, Model model,
		   				  @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
	  if(!model.containsAttribute("store")) {
		 if (storeID == null || "".equals(storeID)) throw new IllegalStateException("storeID in storeEdit was null" );
		 Store store = deliveryService.getStoreById(UUID.fromString(storeID));
	     model.addAttribute("store", store);
	  }
      if ("XMLHttpRequest".equals(requestedWith)) {
          return "/admin/delivery/storeEdit :: editStoreForm";
        }		  
      return "/admin/delivery/storeEdit";		  
	  }	  
	
	@RequestMapping("/store/add")
	public String brandAdd(@RequestParam(value = "ID", required = false) String copyID, Model model,
				           @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws CloneNotSupportedException {
		  Store store = null;
		  if (copyID != null && !"".equals(copyID)) store = deliveryService.cloneStoreById(UUID.fromString(copyID)); 
		  else store = new Store();
	      model.addAttribute("store", store);
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "/admin/delivery/storeEdit :: editStoreForm";
          } 	      
        return "/admin/delivery/storeEdit";		  
	}
	
    @RequestMapping(value="/store/edit", method = RequestMethod.POST )
    public String saveStore(@ModelAttribute @Valid Store store, final BindingResult bindingResult,
		                   @RequestParam(value = "mainImage", required = false)	MultipartFile image,
		                   final RedirectAttributes ra
		                         ) {
        if (bindingResult.hasErrors()) {
        	ra.addFlashAttribute("store", store);
        	ra.addFlashAttribute("org.springframework.validation.BindingResult.store", bindingResult);
            return "redirect:/admin/delivery/store/edit";
        }
        String fileName="";
        String filePath="";
        imageUtil.validateImage(image, "imageUrl", bindingResult);
        if(!bindingResult.hasErrors()) {
	        fileName = new File(image.getOriginalFilename()).getName();
	        filePath = imagePath + fileName;
        	imageUtil.saveImage(filePath, image);		        
	        store.setImageUrl(imageUrl+fileName);
        }

        deliveryService.mergeWithExistingAndUpdateOrCreate(store);	        
        
        return "redirect:/admin/delivery/store";
     }	
	
	
}
