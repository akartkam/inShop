package com.akartkam.inShop.service;

import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring3.context.Beans;
import org.thymeleaf.spring3.context.SpringWebContext;


@Service("EmailService")
public class EmailServiceImpl implements EmailService {

	private static final Log LOG = LogFactory.getLog(EmailServiceImpl.class);
	
	@Autowired
    private ApplicationContext appContext;
	
	@Autowired
	private ServletContext servletContext;

    @Autowired 
    private JavaMailSender mailSender;
    
    @Autowired 
    @Qualifier("emailTemplateEngine")
    private TemplateEngine templateEngine;
    
	@Override
	public void sendSimpleMail(HttpServletRequest request, HttpServletResponse response, 
			String emailAddress, EmailInfo emailInfo, Map<String, Object> vars) {
		final SpringWebContext ctx = new SpringWebContext(request, response, servletContext, LocaleContextHolder.getLocale(),vars, appContext);
	    try {
			final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
	        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, emailInfo.getEncoding());
			message.setSubject(emailInfo.getSubject());
	        message.setFrom(emailInfo.getFromAddress());
	        message.setTo(emailAddress);
	
	        // Create the HTML body using Thymeleaf
	        if (emailInfo.getEmailTemplate() != null && !"".equals(emailInfo.getEmailTemplate())) {
	            final String htmlContent = this.templateEngine.process(emailInfo.getEmailTemplate(), ctx);
	            message.setText(htmlContent, true /* isHtml */);        	
	        } else if (emailInfo.getMessageBody() != null && !"".equals(emailInfo.getMessageBody())) {
	            message.setText(emailInfo.getMessageBody());        	        	
	        }       
	        // Send email
	       	this.mailSender.send(mimeMessage);	
        } catch (MessagingException | MailException ex) {
        	LOG.error(ex);
        }
                
	}

}
