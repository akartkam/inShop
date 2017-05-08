package com.akartkam.inShop.controller;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.akartkam.inShop.service.SitemapService;

@Controller
public class SitemapController {
	  @Autowired
	  private SitemapService sitemapService;
	  
      @Value("#{appProperties['inShop.baseUrl']}")
	  private String baseUrl;
	  
	  @RequestMapping(value="/sitemap.xml", method=RequestMethod.GET, produces = "application/xml")
	  public @ResponseBody String testSiteMap(Model model, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException {
		  String res = sitemapService.createSitemap(baseUrl);
		  return res; 
		  }	

}
