package com.akartkam.inShop.service.content;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.content.Page;

public interface ContentService {
	Page getPageById(UUID id);
	Page createPage(Page page);
	List<Page> getAllPages();
	Page getPageByUrl(String url);
	void softDeletePageById(UUID id);
	void deletePage(Page page);
	void deletePageById(UUID id);
	void mergeWithExistingAndUpdateOrCreate(final Page page);
	Page clonePageById(UUID id) throws CloneNotSupportedException;
	Page loadPageById(UUID id, Boolean lock);

}
