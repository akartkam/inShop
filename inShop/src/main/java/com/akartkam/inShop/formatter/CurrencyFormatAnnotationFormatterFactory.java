package com.akartkam.inShop.formatter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.number.NumberFormatter;


public class CurrencyFormatAnnotationFormatterFactory implements
		AnnotationFormatterFactory<CurrencyFormat> {

	@Override
	public Set<Class<?>> getFieldTypes() {
        return new HashSet<Class<?>>(Arrays.asList(new Class<?>[] {
            Short.class, Integer.class, Long.class, Float.class,
            Double.class, BigDecimal.class, BigInteger.class }));
    }

	@Override
	public Printer<?> getPrinter(CurrencyFormat annotation, Class<?> fieldType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Parser<?> getParser(CurrencyFormat annotation, Class<?> fieldType) {
		// TODO Auto-generated method stub
		return null;
	}
	
    private Formatter<Number> configureFormatterFrom(NumberFormat annotation, Class<?> fieldType) {
                return new NumberFormatter();
    }

}
