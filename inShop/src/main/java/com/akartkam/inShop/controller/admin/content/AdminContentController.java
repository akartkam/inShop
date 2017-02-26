package com.akartkam.inShop.controller.admin.content;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.akartkam.inShop.domain.content.Page;
import com.akartkam.inShop.service.content.ContentService;

@Controller
@RequestMapping("/admin/content")
public class AdminContentController {
	
	@Autowired
	private ContentService contentService;
	
	@ModelAttribute("AllPages")
	public List<Page> getAllPages(){
		return contentService.getAllPages();
	}

}
