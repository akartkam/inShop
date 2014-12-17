package com.akartkam.inShop.domain.product.attribute;


public enum AttribueType {
	DECIMAL("DECIMAL","class for DECIMAL"),
	STRING("STRING","class for STRING"), 
    SLIST("SLIST","class for SLIST");
    
    
    
    public static final AttribueType[] ALL = {DECIMAL, STRING, SLIST};
    
    
    private final String name;
    private final String clazzName;
    
    public static AttribueType forName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null for type");
        }
        if (name.toUpperCase().equals("DECIMAL")) {
            return DECIMAL;
        } else if (name.toUpperCase().equals("STRING")) {
            return STRING;
        } else if (name.toUpperCase().equals("SLIST")) {
            return SLIST;
	    }
        throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any AttribueType");
    }
    
    
    private AttribueType(final String name, final String className) {
        this.name = name;
        this.clazzName = className;
    }
    
    
    public String getName() {
        return this.name;
    }
    
    public String getClassName() {
        return this.clazzName;
    }    
    
    @Override
    public String toString() {
        return getName()+" ["+getClassName()+"]";
    }
}
