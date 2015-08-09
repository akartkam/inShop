package com.akartkam.inShop.exception;

public class ProductNotFoundException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8022493751616689615L;
	
    public ProductNotFoundException(String message) {
		    super(message);
		  }
		  
	public ProductNotFoundException(String message, Throwable cause) {
		    super(message, cause);
		  }

}
