package com.akartkam.inShop.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ControllerAdvice
public class DefaultErrorController{
	private static final Log LOG = LogFactory.getLog(DefaultErrorController.class);
 
	private String path = "/errors";

 @ExceptionHandler(Exception.class)
 public ModelAndView errorHandler(HttpServletRequest req, HttpServletResponse res, Exception ex){
	 LOG.error("Request: " + req.getRequestURL() , ex);
	 
	 ModelAndView model = new ModelAndView();	 
	 if (!model.getModel().containsKey("exception")) {
		 model.addObject("exception", ex);		 
	 }
	 model.addObject("url", req.getRequestURL());
	 model.addObject("timestamp", new Date().toString());
	 model.setViewName(path+"/error-default");
	 res.setStatus(404);
	 return model;
 }

 @RequestMapping(value="/error-default")
 public ModelAndView errorDefault(HttpServletRequest req, HttpServletResponse res){
	 ModelAndView model = new ModelAndView();	 
	 model.setViewName(path+"/error-default");
	 return model;
 }
 
  
}
