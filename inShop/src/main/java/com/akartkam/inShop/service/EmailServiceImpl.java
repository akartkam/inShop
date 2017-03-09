package com.akartkam.inShop.service;

import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@Service("EmailService")
public class EmailServiceImpl implements EmailService {

	private static final Log LOG = LogFactory.getLog(EmailServiceImpl.class);
	
    @Autowired 
    private JavaMailSender mailSender;
    
    @Autowired 
    private TemplateEngine templateEngine;
    
	@Override
	public void sendSimpleMail(HttpServletRequest request, HttpServletResponse response, 
			String emailAddress, EmailInfo emailInfo, Map<String, Object> vars) {

		final WebContext ctx = new WebContext(request, response, request.getServletContext());
		if (vars != null) {
			for (Entry<String, Object> entry: vars.entrySet()) {
				ctx.setVariable(entry.getKey(), entry.getValue());
			}			
		}
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
