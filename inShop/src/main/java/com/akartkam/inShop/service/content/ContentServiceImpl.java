package com.akartkam.inShop.service.content;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.content.PageDAO;
import com.akartkam.inShop.domain.content.AbstractContent;
import com.akartkam.inShop.domain.content.Page;

@Service("ContentService")
@Transactional(readOnly = true)
public class ContentServiceImpl implements ContentService {

	@Autowired
	private PageDAO pageDAO;
	
	@Override
	public Page getPageById(UUID id) {
		return pageDAO.get(id);
	}
	
	@Override
	public Page createPage(Page page) {
		return pageDAO.create(page);
	}

	@Override
	public List<Page> getAllPages() {
		return pageDAO.list();
	}

	@Override
	public Page getPageByUrl(String url) {
		return pageDAO.findByUrl(url);
	}

	@Override
	@Transactional(readOnly = false)
	public void softDeletePageById(UUID id) {
		Page page = getPageById(id);
		if (page != null) {
			page.setEnabled(false);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePage(Page page) {
		pageDAO.delete(page);
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePageById(UUID id) {
		pageDAO.deleteById(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(Page page) {
		if (page == null) return;
		Page exPage = getPageById(page.getId());
		page.buildFullLink(page.getUrlForForm());
		if (exPage != null) {
			exPage.setName(page.getName());
			exPage.setLongDescription(page.getLongDescription());
			exPage.setUrl(page.getUrl());
			exPage.setEnabled(page.isEnabled());
			exPage.setH1(page.getH1());
			exPage.setMetaDescription(page.getMetaDescription());
			exPage.setMetaKeywords(page.getMetaKeywords());
			exPage.setMetaTitle(page.getMetaTitle());
		} else {
			createPage(page);
		}

	}
	
	@Override
	public Page clonePageById(UUID id) throws CloneNotSupportedException{
		Page clonedPage = getPageById(id);
		if (clonedPage == null) return null;
		return clonedPage.clone();
	}
	
	@Override
	public Page loadPageById(UUID id, Boolean lock) {
		return pageDAO.findById(id, lock);
	}	

}
