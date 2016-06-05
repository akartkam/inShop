package com.akartkam.inShop.util;

public class Random10OrderNumberGenerator implements OrderNumberGenerator {
	
	private String prefix; 

	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;

	}

	@Override
	public String genOrderNumber() {
		Long l = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
		StringBuilder sb =  new StringBuilder();
		if (prefix != null) sb.append(prefix);
		sb.append(String.format("%d", l));
		return sb.toString();
	}

}
