package com.akartkam.inShop.exception;

import java.util.UUID;

public class InventoryUnavailableException extends Exception {


    /**
	 * 
	 */
	private static final long serialVersionUID = -5885864122566515190L;

	protected UUID skuId;

    protected Integer quantityRequested;

    protected Integer quantityAvailable;

    public InventoryUnavailableException(String msg) {
        super(msg);
    }
    
    public InventoryUnavailableException(UUID skuId, Integer quantityRequested, Integer quantityAvailable) {
        super();
        this.skuId = skuId;
        this.quantityAvailable = quantityAvailable;
        this.quantityRequested = quantityRequested;
    }

    public InventoryUnavailableException(String arg0, UUID skuId, Integer quantityRequested, Integer quantityAvailable) {
        super(arg0);
        this.skuId = skuId;
        this.quantityAvailable = quantityAvailable;
        this.quantityRequested = quantityRequested;
    }
    
    public InventoryUnavailableException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UUID getSkuId() {
        return skuId;
    }

    public void setSkuId(UUID skuId) {
        this.skuId = skuId;
    }

    public int getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(int quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

}
