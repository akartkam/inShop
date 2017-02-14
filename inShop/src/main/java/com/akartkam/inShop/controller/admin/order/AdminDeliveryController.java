package com.akartkam.inShop.controller.admin.order;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.domain.order.Store;
import com.akartkam.inShop.service.order.DeliveryService;

@Controller
@RequestMapping("/admin/delivery")
public class AdminDeliveryController {
	
	@Autowired
	private DeliveryService deliveryService; 
	
	@ModelAttribute("allStores")
	public List<Store> getAllStores() {
	      return deliveryService.getAllStores();
	}
	
	@RequestMapping(value="/store", method=GET)
	public String stores() {
		  return "/admin/delivery/store"; 
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
}
