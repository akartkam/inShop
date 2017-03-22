package com.akartkam.inShop.domain;

public enum Unit {
	MM("MM","миллиметр","мм"),
	SM("SM","сантиметр","см"),
	KG("KG","килограмм","кг"),
	GR("GR","грамм","г."),
	LT("LT","литр","л."),
	ML("ML","миллилитр","мл"),
	IT("IT","штук","шт"),
	ITUNIT("ITUNIT", "штук в упаковке", "шт/уп"),
	CH("CH","CH","CH");
	
	public static final Unit[] ALL = { MM, SM, KG, GR, LT, ML, IT, ITUNIT, CH};
	
	private final String name;
	private final String fullNameR;
	private final String shortNameR;
	
	private Unit(final String name, final String fullNameR, final String shortNameR) {
		this.name = name;
		this.fullNameR = fullNameR;
		this.shortNameR = shortNameR;
	}
	
	public String getFullNameR() {
		return this.fullNameR;
	}

	public String getShortNameR() {
		return this.shortNameR;
	}

	public String getName() {
        return this.name;
    }	
	
    @Override
    public String toString() {
        return getName();
    }

}
