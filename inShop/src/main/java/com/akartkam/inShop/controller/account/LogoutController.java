package com.akartkam.inShop.controller.account;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
@RequestMapping("/logout")
public class LogoutController {
	  @RequestMapping(value="/success", method=GET)
	  public String admin(HttpSession session) {
		  String result ;
		  Object isadmin = session.getAttribute("isadmin");
		  if (isadmin != null) {
			  result = "redirect:/login"; 
		  } else {
			  result = "redirect:/home.html";  
		  }
		  session.invalidate();
		  return result; 
		  }
}