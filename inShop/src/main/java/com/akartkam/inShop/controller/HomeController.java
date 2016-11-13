package com.akartkam.inShop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


@Controller
public class HomeController extends AbstractController {
	
	private static final Log LOG = LogFactory.getLog(HomeController.class);

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LOG.info(request.getRemoteHost());
		ModelAndView model = new ModelAndView("/layouts/home");
		return model;
	}

}
