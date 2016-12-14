package com.akartkam.inShop.formatter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.number.AbstractNumberFormatter;



public class CurrencyFormatAnnotationFormatterFactory implements
		AnnotationFormatterFactory<CurrencyFormat> {
	
	private String currencySymbol;
	private String groupingSeparator;
	private String monetaryDecimalSeparator;
	private CurrencyNumberFormatter currencyNumberFormatter;
	private int fractionDigits = 2;
	
	public CurrencyFormatAnnotationFormatterFactory() {
		currencyNumberFormatter = new CurrencyNumberFormatter();
	}
	
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
	public Set<Class<?>> getFieldTypes() {
        return new HashSet<Class<?>>(Arrays.asList(new Class<?>[] {
            Short.class, Integer.class, Long.class, Float.class,
            Double.class, BigDecimal.class, BigInteger.class }));
    }

	@Override
	public Printer<?> getPrinter(CurrencyFormat annotation, Class<?> fieldType) {
		return configureFormatterFrom(annotation, fieldType);
	}

	@Override
	public Parser<?> getParser(CurrencyFormat annotation, Class<?> fieldType) {
		return configureFormatterFrom(annotation, fieldType);
	}
	
    private Formatter<Number> configureFormatterFrom(CurrencyFormat annotation, Class<?> fieldType) {
        return currencyNumberFormatter;
    }
    
    private class CurrencyNumberFormatter extends AbstractNumberFormatter {

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
    

}
