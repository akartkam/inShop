package com.akartkam.inShop.formatter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.number.AbstractNumberFormatter;



public class CurrencyFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<CurrencyFormat> {
	
	@Autowired
	private AbstractNumberFormatter currencyNumberFormatter;
	
	public CurrencyFormatAnnotationFormatterFactory() {
		currencyNumberFormatter = new CurrencyNumberFormatter();
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
    

}
