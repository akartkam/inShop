package com.akartkam.inShop.app;

import java.nio.charset.Charset;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;



public class Common {

	public static void main(String[] args) {
		    System.out.println(Charset.defaultCharset().name());
			System.out.println(Currency.getInstance(Locale.getDefault()).getDisplayName());
			System.out.println(Currency.getInstance(Locale.getDefault()).getCurrencyCode());
			System.out.println(Currency.getInstance(Locale.getDefault()).getSymbol());
			System.out.println(Currency.getInstance(Locale.getDefault()).getDefaultFractionDigits());
			
			NumberFormat df = NumberFormat.getCurrencyInstance();
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setCurrencySymbol("р.");
			dfs.setGroupingSeparator(' ');
			dfs.setMonetaryDecimalSeparator(',');
			((DecimalFormat)df).setDecimalFormatSymbols(dfs);
			System.out.println(df.format(2567.56));
			 
			String fval = StringUtils.rightPad(StringUtils.leftPad("7", 2, "%"), 3, "%");

			System.out.println(StringUtils.leftPad("7", 2, "%"));
			System.out.println(fval);
			
	}

}

