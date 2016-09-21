package com.akartkam.inShop.util;

public class CommonUtil {
	  
	private CommonUtil () {};
	  
	public static int nullSafeIntegerToPrimitive(Integer value) {
		int res = 0;
		if (value != null) res = value;
		return res;
	}
}
