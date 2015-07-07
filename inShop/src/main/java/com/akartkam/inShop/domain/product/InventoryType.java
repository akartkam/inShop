package com.akartkam.inShop.domain.product;

public enum InventoryType {
	ALWAYS_AVAILABLE ("Доступен всегда"),
	UNAVAILABLE ("Не доступен"),
	CHECK_QUANTITY ("Отслеживать количество");
	
	public static final InventoryType[] ALL = {ALWAYS_AVAILABLE, UNAVAILABLE, CHECK_QUANTITY};
	
    private final String name;
    
    public static InventoryType forName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null for type");
        }
        if (name.toUpperCase().equals("ALWAYS_AVAILABLE")) {
            return ALWAYS_AVAILABLE;
        } else if (name.toUpperCase().equals("UNAVAILABLE")) {
            return UNAVAILABLE;
        } else if (name.toUpperCase().equals("CHECK_QUANTITY")) {
            return CHECK_QUANTITY;
	    }
        throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any InventoryType");
    }    
    
    private InventoryType (final String name) {
    	this.name = name;
    }
    
    public String getName() {
		return name;
	}

}
