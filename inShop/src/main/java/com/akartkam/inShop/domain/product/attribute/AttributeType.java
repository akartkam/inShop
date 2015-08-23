package com.akartkam.inShop.domain.product.attribute;


public enum AttributeType {
	DECIMAL("DECIMAL","com.akartkam.inShop.domain.product.attribute.AttributeDecimal",
					  "com.akartkam.inShop.domain.product.attribute.AttributeDecimalValue"),
	STRING("STRING","com.akartkam.inShop.domain.product.attribute.AttributeString",
					"com.akartkam.inShop.domain.product.attribute.AttributeStringValue"), 
    SLIST("SLIST","com.akartkam.inShop.domain.product.attribute.AttributeSList",
    			  "com.akartkam.inShop.domain.product.attribute.AttributeSListValue");
    
    
    
    public static final AttributeType[] ALL = {DECIMAL, STRING, SLIST};
    
    
    private final String name;
    private final String clazzAttributeName;
    private final String clazzAttributeValueName;
    
    public static AttributeType forName(final String name) {
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
        throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any AttributeType");
    }
    
    
    private AttributeType(final String name, final String clazzAttributeName, final String clazzAttributeValueName) {
        this.name = name;
        this.clazzAttributeName = clazzAttributeName;
        this.clazzAttributeValueName = clazzAttributeValueName;
    }
    
    
    public String getName() {
        return this.name;
    }
    
    public String getClassAttributeName() {
        return this.clazzAttributeName;
    }   
    
    public String getClassAttributeValueName() {
        return this.clazzAttributeValueName;
    } 
    
    
    @Override
    public String toString() {
        return getName();
    }
}
