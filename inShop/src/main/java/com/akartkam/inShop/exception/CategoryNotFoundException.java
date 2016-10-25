package com.akartkam.inShop.exception;

public class CategoryNotFoundException extends RuntimeException {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 549103547608597316L;

	public CategoryNotFoundException (String message) {
	    super(message);
	}

    public CategoryNotFoundException(String message, Throwable cause) {
	    super(message, cause);
	}
}
