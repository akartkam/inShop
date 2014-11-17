package com.akartkam.inShop.app;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import com.akartkam.inShop.domain.GeneratorId;

public class ComparableExample implements Comparable<ComparableExample> {

	public UUID id = GeneratorId.createId();
	public UUID prev;
	public UUID next;
	
	public String name;
	
	public ComparableExample (String name) {
		this.name = name;
	}
	
	public String toString(){
        return "Name: "+this.name+" Id: "+this.id+" prev: "+this.prev;
    }
	
	@Override
	public int compareTo(ComparableExample arg0) {
		if ((this.prev == null) || (this.id.equals(arg0.prev))) {return -1;}
		
		if ((arg0.prev == null) || (this.prev.equals(arg0.id))) {return 1;}
		
		return 0; 
	}
	
	public static void main(String a[]){
		
		ComparableExample ce1 = new ComparableExample("c1");
		ComparableExample ce2 = new ComparableExample("c2");
		ComparableExample ce3 = new ComparableExample("c3");
		ComparableExample ce4 = new ComparableExample("c4");
		
		ce2.prev = ce1.id;
		ce4.prev = ce2.id;
		ce3.prev = ce4.id;
		
		//SortedSet<ComparableExample> list = new TreeSet<ComparableExample>();
		List<ComparableExample> list = new LinkedList<ComparableExample>();
		list.add(ce3);
		list.add(ce1);
		list.add(ce2);
		list.add(ce4);
		
		for (ComparableExample ce : list ) {
			System.out.println(ce);
		}
		
		Collections.sort(list);
		
		for (ComparableExample ce : list ) {
			System.out.println(ce);
		}		
		
	}

}
