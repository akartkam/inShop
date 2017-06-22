package com.akartkam.inShop.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.akartkam.inShop.formbean.ProductFilterDTO;
import com.akartkam.inShop.service.SitemapService;

@Controller
@RequestMapping("/admin/catalog/test")
public class TestController {
	  private static final Log LOG = LogFactory.getLog(TestController.class);
	 
	  @Autowired
	  private SitemapService sitemapService;

      @Value("#{appProperties['inShop.baseUrl']}")
	  private String baseUrl;
	  
	  @RequestMapping(value="/product-ajax-test", method=GET)
	  public String test() {
		  return "/admin/catalog/product-ajax-load-test"; 
		  }	
	  @RequestMapping(value="/sitemap.xml", method=GET, produces = "application/xml")
	  public @ResponseBody String testSiteMap(Model model, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException {
		  String res = sitemapService.createSitemap(baseUrl);
		  return res; 
		  }	
	  @RequestMapping(value="/test-product-filter", method=POST)
	  public String testProductFilter(final @ModelAttribute ProductFilterDTO productFilterDTO, Model model) {
		  model.addAttribute("filterDTO", productFilterDTO);
		  return "/catalog/category";
		  
	  }

	  
}
