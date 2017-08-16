package com.akartkam.inShop.domain.rating;

public enum RatingType {
		
		COMMON("COMMON"),
		PRODUCT("PRODUCT");
   
	    
	    public static final RatingType[] ALL = { COMMON, PRODUCT};
	    
	    
	    private final String name;

	    
	    public static RatingType forName(final String name) {
	        if (name == null) {
	            throw new IllegalArgumentException("Name cannot be null for RatingType");
	        }
	        if (name.toUpperCase().equals("COMMON")) {
	            return COMMON;
	        } else if (name.toUpperCase().equals("PRODUCT")) {
	            return PRODUCT;
	        } 
	        throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any RatingType");
	    }
	    
	    
	    private RatingType(final String name) {
	        this.name = name;
	    }
	    
	    
	    public String getName() {
	        return this.name;
	    }
	    
	    @Override
	    public String toString() {
	        return getName();
	    }
}
	
