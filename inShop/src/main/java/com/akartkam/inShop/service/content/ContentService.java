package com.akartkam.inShop.service.content;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.content.AbstractContent;
import com.akartkam.inShop.domain.content.NewsPage;
import com.akartkam.inShop.domain.content.Page;

public interface ContentService {
	Page getPageById(UUID id);
	AbstractContent createPage(AbstractContent page);
	List<Page> getAllPages();
	Page getPageByUrl(String url);
	void softDeletePageById(UUID id);
	void deletePage(Page page);
	void deletePageById(UUID id);
	void mergeWithExistingAndUpdateOrCreate(final AbstractContent page);
	Page clonePageById(UUID id) throws CloneNotSupportedException;
	Page loadPageById(UUID id, Boolean lock);
	List<NewsPage> getAllNewsPages();
	List<NewsPage> getActualNewsPages();
	NewsPage getNewsPageById(UUID id);
	NewsPage cloneNewsPageById(UUID id) throws CloneNotSupportedException;
	NewsPage loadNewsPageById(UUID id, Boolean lock);
	void deleteNewsPage(NewsPage page);
	void softDeleteNewsPageById(UUID id);
	AbstractContent getPageById(AbstractContent page);
	NewsPage getNewsPageByUrl(String url);
}
