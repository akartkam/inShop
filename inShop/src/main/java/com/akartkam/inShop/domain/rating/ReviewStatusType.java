package com.akartkam.inShop.domain.rating;

public enum ReviewStatusType {
	PENDING("PENDING"),
	APPROVED("APPROVED"),
	REJECT("REJECT");

    
    public static final ReviewStatusType[] ALL = { PENDING, APPROVED, REJECT};
    
    
    private final String name;

    
    public static ReviewStatusType forName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null for ReviewStatusType");
        }
        if (name.toUpperCase().equals("PENDING")) {
            return PENDING;
        } else if (name.toUpperCase().equals("APPROVED")) {
            return APPROVED;
        } else if (name.toUpperCase().equals("REJECT")) {
            return REJECT;
        } else 
        throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any ReviewStatusType");
    }
    
    
    private ReviewStatusType(final String name) {
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
