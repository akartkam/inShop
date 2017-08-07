package com.akartkam.inShop.service.content;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.content.ContentDAO;
import com.akartkam.inShop.domain.content.AbstractContent;
import com.akartkam.inShop.domain.content.NewsPage;
import com.akartkam.inShop.domain.content.Page;

@Service("ContentService")
@Transactional(readOnly = true)
public class ContentServiceImpl implements ContentService {

	private ContentDAO<Page> pageDAO;
	private ContentDAO<NewsPage> newsPageDAO;
	
	
	@Autowired
	public void setPageDAO (ContentDAO<Page> pageDAO) {
		this.pageDAO = pageDAO;
		this.pageDAO.setClaszz(Page.class);
	}

	@Autowired
	public void setNewsPageDAO(ContentDAO<NewsPage> pageDAO) {
		this.newsPageDAO = pageDAO;
		this.newsPageDAO.setClaszz(NewsPage.class);
	}	
	
	@Override
	public Page getPageById(UUID id) {
		return pageDAO.get(id);
	}
	
	@Override
	public AbstractContent createPage(AbstractContent page) {
		if (page instanceof Page) return pageDAO.create((Page)page);
		if (page instanceof NewsPage) return newsPageDAO.create((NewsPage)page);
		throw new IllegalArgumentException("Bad argument in createPage");
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
	public void mergeWithExistingAndUpdateOrCreate(AbstractContent page) {
		if (page == null) return;
		AbstractContent exPage = getPageById(page);
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
			setPageProps(page, exPage);
		} else {
			createPage(page);
		}

	}
	
	private void setPageProps(AbstractContent page, AbstractContent exPage) {
		if (page instanceof NewsPage && exPage instanceof NewsPage) {
			((NewsPage)exPage).setDescription(((NewsPage)page).getDescription());
			((NewsPage)exPage).setSubmitDate(((NewsPage)page).getSubmitDate());
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

	@Override
	public List<NewsPage> getAllNewsPages() {
		return newsPageDAO.list();
	}

	@Override
	public NewsPage getNewsPageById(UUID id) {
		return newsPageDAO.get(id);
	}

	@Override
	public NewsPage cloneNewsPageById(UUID id) throws CloneNotSupportedException {
		NewsPage clonedPage = getNewsPageById(id);
		if (clonedPage == null) return null;
		return clonedPage.clone();
	}

	@Override
	public NewsPage loadNewsPageById(UUID id, Boolean lock) {
		return newsPageDAO.findById(id, lock);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteNewsPage(NewsPage page) {
		newsPageDAO.delete(page);
	}

	@Override
	@Transactional(readOnly = false)
	public void softDeleteNewsPageById(UUID id) {
		NewsPage page = loadNewsPageById(id, false);
		if (page != null) {
			page.setEnabled(false);
		}
		
	}

	@Override
	public AbstractContent getPageById(AbstractContent page) {
		if (page instanceof Page) return pageDAO.get(page.getId());
		if (page instanceof NewsPage) return newsPageDAO.get(page.getId());
		return null;
	}	

}
