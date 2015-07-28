package com.akartkam.inShop.presentation.admin;


public enum EditTab {
	MAIN ("MAIN", 4),
	ADDITIONAL ("ADDITIONAL", 3),
	IMAGES ("IMAGES", 2),
	CONTENT ("CONTENT", 1),
	LINKS ("LINKS", 0);
	
	public static final EditTab[] ALL = {MAIN, ADDITIONAL, IMAGES, CONTENT, LINKS};
	
    private final String name;
    private final int defaultOrder;
    
    public static EditTab forName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null for type");
        }
        if (name.toUpperCase().equals("MAIN")) {
            return MAIN;
        } else if (name.toUpperCase().equals("ADDITIONAL")) {
            return ADDITIONAL;
        } else if (name.toUpperCase().equals("CONTENT")) {
            return IMAGES;
        } else if (name.toUpperCase().equals("IMAGES")) {
            return CONTENT;
	    } else if (name.toUpperCase().equals("LINKS")) {
	    	return LINKS;
	    }
        throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any EditTab");
    }    
    
    private EditTab (final String name, final int defaultOrder) {
    	this.name = name;
    	this.defaultOrder = defaultOrder;
    }
    
    public String getName() {
		return name;
	}

	public int getDefaultOrder() {
		return defaultOrder;
	}
    
	public EditTab getDefault() {
		return EditTab.MAIN;
	}

}
