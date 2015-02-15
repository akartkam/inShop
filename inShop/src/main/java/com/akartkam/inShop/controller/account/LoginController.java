package com.akartkam.inShop.controller.account;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring.support.Layout;

@Controller
@Layout("authentication/login")
public class LoginController {
	@RequestMapping(value="/login", method=GET)
	  public String Login() {return "authentication/login"; }
}
