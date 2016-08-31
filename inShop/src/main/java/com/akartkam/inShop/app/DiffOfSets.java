package com.akartkam.inShop.app;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;



public class DiffOfSets {

	public static void main(String[] args) {
		Set<Number> s1= new HashSet<Number>();
		Set<Number> s2= new HashSet<Number>();
		s1.add(1);
		s1.add(2);
		s1.add(3);
		s1.add(4);
		
		s2.add(3);
		s2.add(4);
				
		Collection<Number> res = CollectionUtils.disjunction(s2,s1);
		
		System.out.println(res);
		
		System.out.print(new BigDecimal("15E-38").toPlainString());

	}

}
