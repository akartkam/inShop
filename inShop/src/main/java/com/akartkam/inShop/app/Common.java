package com.akartkam.inShop.app;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.UUID;



public class Common {

	public static void main(String[] args) {
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
			
			System.out.println(UUID.randomUUID().toString());
			
			String i = "1,0";
			boolean r = true;
			try { 
			  int ii = Integer.parseInt(i);
			  
			} catch (NumberFormatException e) {
				r = false;
			}
			
			System.out.println(r);
			
	}

}

