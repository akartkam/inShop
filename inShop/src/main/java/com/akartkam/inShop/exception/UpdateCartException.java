package com.akartkam.inShop.exception;

public class UpdateCartException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3829167209748005698L;

	public UpdateCartException() {
        super();
    }

    public UpdateCartException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateCartException(String message) {
        super(message);
    }

    public UpdateCartException(Throwable cause) {
        super(cause);
    }

}
