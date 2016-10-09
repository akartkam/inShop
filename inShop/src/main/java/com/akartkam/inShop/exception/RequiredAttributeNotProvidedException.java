package com.akartkam.inShop.exception;

public class RequiredAttributeNotProvidedException extends RuntimeException {

    

    /**
	 * 
	 */
	private static final long serialVersionUID = -2934870503194980197L;
	protected String attributeName;

    public RequiredAttributeNotProvidedException(String message, String attributeName) {
        super(message);
        setAttributeName(attributeName);
    }

    public RequiredAttributeNotProvidedException(String message, String attributeName, Throwable cause) {
        super(message, cause);
        setAttributeName(attributeName);
    }

    public RequiredAttributeNotProvidedException(String attributeName) {
        super("The attribute " + attributeName + " was not provided");
        setAttributeName(attributeName);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }


}
