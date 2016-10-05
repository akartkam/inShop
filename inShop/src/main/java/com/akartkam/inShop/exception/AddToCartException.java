package com.akartkam.inShop.exception;

public class AddToCartException extends Exception {


    /**
	 * 
	 */
	private static final long serialVersionUID = -2687843550603874224L;

	public AddToCartException() {
        super();
    }

    public AddToCartException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddToCartException(String message) {
        super(message);
    }

    public AddToCartException(Throwable cause) {
        super(cause);
    }

}
