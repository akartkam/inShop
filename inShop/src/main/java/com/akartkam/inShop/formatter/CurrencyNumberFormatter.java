package com.akartkam.inShop.formatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.format.number.AbstractNumberFormatter;

public class CurrencyNumberFormatter extends AbstractNumberFormatter {

	private String currencySymbol;
	private String groupingSeparator;
	private String monetaryDecimalSeparator;
	private int fractionDigits = 2;
	
	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getGroupingSeparator() {
		return groupingSeparator;
	}

	public void setGroupingSeparator(String groupingSeparator) {
		this.groupingSeparator = groupingSeparator;
	}

	public String getMonetaryDecimalSeparator() {
		return monetaryDecimalSeparator;
	}

	public void setMonetaryDecimalSeparator(String monetaryDecimalSeparator) {
		this.monetaryDecimalSeparator = monetaryDecimalSeparator;
	}

	
	@Override
	protected NumberFormat getNumberFormat(Locale locale) {
		NumberFormat df = NumberFormat.getCurrencyInstance();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		if (currencySymbol != null && !"".equals(currencySymbol)) dfs.setCurrencySymbol(currencySymbol);
		if (groupingSeparator != null && !"".equals(groupingSeparator)) dfs.setGroupingSeparator(groupingSeparator.charAt(0));
		if (monetaryDecimalSeparator != null && !"".equals(monetaryDecimalSeparator)) dfs.setMonetaryDecimalSeparator(monetaryDecimalSeparator.charAt(0));
		((DecimalFormat)df).setMinimumFractionDigits(fractionDigits);
		((DecimalFormat)df).setDecimalFormatSymbols(dfs);
		return df;
	}

}
