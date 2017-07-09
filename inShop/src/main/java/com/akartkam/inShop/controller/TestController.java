package com.akartkam.inShop.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.akartkam.inShop.dao.product.ProductDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.formbean.ProductFilterDTO;
import com.akartkam.inShop.service.SitemapService;
import com.akartkam.inShop.service.product.CategoryService;
import com.akartkam.inShop.service.product.ProductService;

@Controller
@RequestMapping("/admin/catalog/test")
public class TestController {
	  private static final Log LOG = LogFactory.getLog(TestController.class);
	 
	  @Autowired
	  private SitemapService sitemapService;

      @Value("#{appProperties['inShop.baseUrl']}")
	  private String baseUrl;
      
      @Autowired
      private ProductService productService;
      
      @Autowired
      private CategoryService categoryService;      
	  
	  @RequestMapping(value="/product-ajax-test", method=GET)
	  public String test() {
		  return "/admin/catalog/product-ajax-load-test"; 
		  }	
	  @RequestMapping(value="/sitemap.xml", method=GET, produces = "application/xml")
	  public @ResponseBody String testSiteMap(Model model, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException {
		  String res = sitemapService.createSitemap(baseUrl);
		  return res; 
		  }	
  
}
