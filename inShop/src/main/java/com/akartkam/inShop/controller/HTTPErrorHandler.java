package com.akartkam.inShop.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HTTPErrorHandler{
 
 String path = "/errors";

 @ExceptionHandler
 @RequestMapping(value="/error-default")
 public String error400(HttpServletRequest req, Exception e, Model model){
	 model.addAttribute("exception", e);
	 model.addAttribute("url", req.getRequestURL());
	 model.addAttribute("timestamp", new Date().toString());
  return path+"/error-default";
 }

  
  
}
