package com.akartkam.inShop.service;

import java.io.Serializable;

public class EmailInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1396223806751419692L;
    private String emailTemplate;
    private String subject;
    private String fromAddress;
    private String messageBody;
    private String encoding = "UTF8";
	public String getEmailTemplate() {
		return emailTemplate;
	}
	public void setEmailTemplate(String emailTemplate) {
		this.emailTemplate = emailTemplate;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
    
    

}
