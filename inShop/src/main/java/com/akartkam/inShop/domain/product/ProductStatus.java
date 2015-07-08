package com.akartkam.inShop.domain.product;

public enum ProductStatus {
	HIT ("Хит продаж"),
	NEW ("Новинка"),
	ACTION ("Акция");
	
	public static final ProductStatus[] ALL = {HIT, NEW, ACTION};
	
    private final String name;
    
    public static ProductStatus forName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null for type");
        }
        if (name.toUpperCase().equals("HIT")) {
            return HIT;
        } else if (name.toUpperCase().equals("NEW")) {
            return NEW;
        } else if (name.toUpperCase().equals("ACTION")) {
            return ACTION;
	    }
        throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any ProductStatus");
    }    
    
    private ProductStatus (final String name) {
    	this.name = name;
    }
    
    public String getName() {
		return name;
	}

}
