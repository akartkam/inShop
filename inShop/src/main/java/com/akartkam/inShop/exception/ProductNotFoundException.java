package com.akartkam.inShop.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8022493751616689615L;
	private UUID productId;
	
    public ProductNotFoundException(String message) {
		    super(message);
		  }
		  
    public ProductNotFoundException(String message, UUID productId) {
	    super(message);
	    this.productId = productId;
	  }
    
    public ProductNotFoundException(String message, Throwable cause) {
		    super(message, cause);
		  }

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}
	
	

}
