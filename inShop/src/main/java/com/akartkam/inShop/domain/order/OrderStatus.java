package com.akartkam.inShop.domain.order;

public enum OrderStatus {
	INCOMPLETE,
	NEW,
	IN_PROCESS ,
	PAID ,
	IN_DELIVERY,
	COMPLITED ,
	CANCELLED ;
	public static final OrderStatus[] ALL = {INCOMPLETE, NEW, IN_PROCESS, PAID, IN_DELIVERY, COMPLITED, CANCELLED};
	
    
    public static OrderStatus forName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null for type");
        }
        if (name.toUpperCase().equals("INCOMPLETE")) {
            return INCOMPLETE;
        } else if (name.toUpperCase().equals("NEW")) {
            return NEW;
        } else if (name.toUpperCase().equals("IN_PROCESS")) {
            return IN_PROCESS;
        } else if (name.toUpperCase().equals("COMPLITED")) {
            return COMPLITED;
        } else if (name.toUpperCase().equals("PAID")) {
            return PAID;
        } else if (name.toUpperCase().equals("IN_DELIVERY")) {
            return IN_DELIVERY;                        
        } else if (name.toUpperCase().equals("CANCELLED")) {
            return CANCELLED;
	    }
        throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any OrderStatus");
    }
    

}
