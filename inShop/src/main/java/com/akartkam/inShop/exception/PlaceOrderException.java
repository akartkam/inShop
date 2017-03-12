package com.akartkam.inShop.exception;

public class PlaceOrderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8634685045551086854L;
	
	public PlaceOrderException() {
        super();
    }

    public PlaceOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlaceOrderException(String message) {
        super(message);
    }

    public PlaceOrderException(Throwable cause) {
        super(cause);
    }	

}
