package com.akartkam.inShop.service;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.product.CategoryDAO;
import com.akartkam.inShop.dao.product.ProductDAO;
import com.akartkam.inShop.domain.content.Page;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.service.content.ContentService;
import com.akartkam.inShop.service.product.ProductService;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;



@Service("SitemapService")
@Transactional(readOnly = true)
public class SitemapServiceImpl implements SitemapService {


	@Value("#{entityUrlPrefixes.getProperty(T(com.akartkam.inShop.util.Constants).PAGE_CLASS)}")
	private String pagePrefix;

	@Value("#{entityUrlPrefixes.getProperty(T(com.akartkam.inShop.util.Constants).CATEGORY_CLASS)}")
	private String categoryPrefix;
	
	@Value("#{entityUrlPrefixes.getProperty(T(com.akartkam.inShop.util.Constants).PRODUCT_CLASS)}")
	private String productPrefix;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private CategoryDAO categoryDAO;

	
	
	@Override
	public String createSitemap(String baseUrl) throws MalformedURLException {
		if (baseUrl == null || "".equals(baseUrl)) return null;
		WebSitemapGenerator sitemap = new WebSitemapGenerator(baseUrl);
		sitemap.addUrl(baseUrl);
		String pagesUrlPrefix = baseUrl+"/"+pagePrefix;
		String productUrlPrefix = baseUrl+"/"+productPrefix;
		String categoryUrlPrefix = baseUrl+"/"+categoryPrefix;
		for (String pUrl : productDAO.findAllProductUrls()) {
			WebSitemapUrl url = new WebSitemapUrl.Options(productUrlPrefix+pUrl)
		                            .priority(1.0).changeFreq(ChangeFreq.WEEKLY).build();
			sitemap.addUrl(url);
		}
		for (Category ctg  : categoryDAO.list()) {
			if (ctg.isEnabled() && ctg.hasParentCategory()) {
				WebSitemapUrl url = new WebSitemapUrl.Options(categoryUrlPrefix+ctg.getUrl())
                .priority(0.7).changeFreq(ChangeFreq.WEEKLY).build();
				sitemap.addUrl(url);				
			}
		}

		for(Page page : contentService.getAllPages()){
			WebSitemapUrl url = new WebSitemapUrl.Options(pagesUrlPrefix+page.getUrl())
            .priority(0.5).changeFreq(ChangeFreq.WEEKLY).build();

			sitemap.addUrl(url);
		}
		return String.join("", sitemap.writeAsStrings());
	}

}
