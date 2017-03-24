package com.akartkam.inShop.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class DefaultErrorController{
	private static final Log LOG = LogFactory.getLog(DefaultErrorController.class);
 
	private String path = "/errors";

 @ExceptionHandler(Exception.class)
 @RequestMapping(value="/error-default")
 public ModelAndView error(HttpServletRequest req, Exception e){
	 if (e != null ) LOG.error("",e);
	 ModelAndView model = new ModelAndView();
	 if (!model.getModel().containsKey("e")) {
		 model.addObject("exception", e);		 
	 }
	 model.addObject("url", req.getRequestURL());
	 model.addObject("timestamp", new Date().toString());
	 model.setViewName(path+"/error-default");
	 return model;
 }

  
  
}
