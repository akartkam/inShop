package com.akartkam.inShop.controller.admin.content;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.File;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.domain.content.Page;
import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.service.content.ContentService;

@Controller
@RequestMapping("/admin/content/page")
public class AdminContentController {
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
    private MessageSource messageSource;
	
	
	@ModelAttribute("AllPages")
	public List<Page> getAllPages(){
		return contentService.getAllPages();
	}
	
	@RequestMapping(method=GET)
	public String page() {
		  return "/admin/content/page"; 
	}
	
    @RequestMapping("/edit")
    public String pageEdit(@RequestParam(value = "ID", required = false) String pageID, Model model,
		   				  @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		if(!model.containsAttribute("page")) {
			 if (pageID == null || "".equals(pageID)) throw new IllegalStateException("pageID in pageEdit was null" );
			 Page page = contentService.getPageById(UUID.fromString(pageID));
		     model.addAttribute("page", page);
		}
	    if ("XMLHttpRequest".equals(requestedWith)) {
	        return "/admin/content/pageEdit :: editPageForm";
	      }		  
	    return "/admin/content/pageEdit";		  
	}
    
    @RequestMapping("/add")
    public String pageAdd(@RequestParam(value = "ID", required = false) String copyID, Model model,
			                @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws CloneNotSupportedException {
	    Page page = null;
	    if (copyID != null && !"".equals(copyID)) page = contentService.clonePageById(UUID.fromString(copyID)); 
	    else page = new Page();
        model.addAttribute("page", page);
	    if ("XMLHttpRequest".equals(requestedWith)) {
	        return "/admin/content/pageEdit :: editPageForm";
	      } 	      
	    return "/admin/content/pageEdit";		  
	}	

    @RequestMapping(value="/delete", method = RequestMethod.POST)
	public String pageDelete(@RequestParam(value = "ID", required = false) String ID, 
	                          @RequestParam(value = "phisycalDelete", required = false) Boolean phisycalDelete,
				              final RedirectAttributes ra) {
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  if (phisycalDelete != null && phisycalDelete)  {
			  Page page = contentService.loadPageById(UUID.fromString(ID), false);
			  if(page.canRemove() && authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
				  contentService.deletePage(page);   
			  } else {
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", new String[] {"страницу"} , null));
				  ra.addAttribute("error", true);
			  }

		  } else {
			  contentService.softDeletePageById(UUID.fromString(ID));
		  }
        return "redirect:/admin/content/page";		  
	}    

    @RequestMapping(value="/edit", method = RequestMethod.POST )
    public String pageBrand(@ModelAttribute @Valid Page page,
		                   final BindingResult bindingResult,
		                   final RedirectAttributes ra
		                         ) {
        if (bindingResult.hasErrors()) {
        	ra.addFlashAttribute("page", page);
        	ra.addFlashAttribute("org.springframework.validation.BindingResult.page", bindingResult);
            return "redirect:/admin/content/page/edit";
        }
        contentService.mergeWithExistingAndUpdateOrCreate(page);	        
        return "redirect:/admin/content/page";
     }
    
}
