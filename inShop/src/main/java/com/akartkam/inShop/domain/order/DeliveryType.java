package com.akartkam.inShop.domain.order;

public enum DeliveryType {
		SELF_DELIVERY ,
		REQUIRED_SPEC ;
		public static final DeliveryType[] ALL = {SELF_DELIVERY, REQUIRED_SPEC};
		
	    
	    public static DeliveryType forName(final String name) {
	        if (name == null) {
	            throw new IllegalArgumentException("Name cannot be null for type");
	        }
	        if (name.toUpperCase().equals("SELF_DELIVERY")) {
	            return SELF_DELIVERY;
	        } else if (name.toUpperCase().equals("REQUIRED_SPEC")) {
	            return REQUIRED_SPEC;
	        } else {
	          throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any DeliveryType");
	        }
       }
}