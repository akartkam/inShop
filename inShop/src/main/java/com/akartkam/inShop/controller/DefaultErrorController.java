package com.akartkam.inShop.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DefaultErrorController{
 
 String path = "/errors";

 @ExceptionHandler(Exception.class)
 @RequestMapping(value="/error-default")
 public ModelAndView error(HttpServletRequest req, Exception e){
	 ModelAndView model = new ModelAndView();
	 model.addObject("exception", e);
	 model.addObject("url", req.getRequestURL());
	 model.addObject("timestamp", new Date().toString());
	 model.setViewName(path+"/error-default");
	 return model;
 }

  
  
}
