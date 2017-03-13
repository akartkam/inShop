package com.akartkam.inShop.service;

import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface EmailService  {
	public void sendSimpleMail(HttpServletRequest request, HttpServletResponse  response, 
			                   String emailAddress, EmailInfo emailInfo, Map<String, Object> vars);
}
