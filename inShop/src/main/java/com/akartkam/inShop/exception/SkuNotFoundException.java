package com.akartkam.inShop.exception;

public class SkuNotFoundException extends RuntimeException {
		
	 /**
	 * 
	 */
	private static final long serialVersionUID = 8001896404872490043L;

	public SkuNotFoundException(String message) {
			    super(message);
			  }
			  
	public SkuNotFoundException(String message, Throwable cause) {
			    super(message, cause);
			  }

}
