package com.akartkam.inShop.exception;

public abstract class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7065748258384305356L;

	protected BusinessException(final String message) {
		        super(message);
		    }
		
	protected BusinessException(final String message, final Throwable cause) {
		        super(message, cause);
		    }	
}

