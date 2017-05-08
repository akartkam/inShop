package com.akartkam.inShop.service;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.product.ProductDAO;
import com.akartkam.inShop.domain.content.Page;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.service.content.ContentService;
import com.akartkam.inShop.service.product.ProductService;
import com.redfin.sitemapgenerator.WebSitemapGenerator;



@Service("SitemapService")
@Transactional(readOnly = true)
public class SitemapServiceImpl implements SitemapService {

	@Value("#{entityUrlPrefixes.getProperty(T(com.akartkam.inShop.util.Constants).PAGE_CLASS)}")
	private String pagePrefix;

	@Value("#{entityUrlPrefixes.getProperty(T(com.akartkam.inShop.util.Constants).PRODUCT_CLASS)}")
	private String productPrefix;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private ProductDAO productDAO;
	
	@Override
	public String createSitemap(String baseUrl) throws MalformedURLException {
		if (baseUrl == null || "".equals(baseUrl)) return null;
		WebSitemapGenerator sitemap = new WebSitemapGenerator(baseUrl);
		sitemap.addUrl(baseUrl);
		String pagesUrlPrefix = baseUrl+"/"+pagePrefix;
		for(Page page : contentService.getAllPages()){
			sitemap.addUrl(pagesUrlPrefix+page.getUrl());
		}
		String productUrlPrefix = baseUrl+"/"+productPrefix;
		for (String pUrl : productDAO.findAllProductUrls()) {
			sitemap.addUrl(productUrlPrefix+pUrl);
		}
		return String.join("", sitemap.writeAsStrings());
	}

}
