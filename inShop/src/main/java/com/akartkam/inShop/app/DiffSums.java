package com.akartkam.inShop.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DiffSums {
	
	public static void main(String[] args) throws NumberFormatException, IOException  
 {
		
		double S;
		int N;
		int Si, Si1, Si2, S1;
		
		int p1=0,p2=0, q1=0, q2=0, pc=1;
		
		String tmp;
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println ("Введите основную сумму(2 цифры после запятой)");
		S = Double.parseDouble(in.readLine());

		System.out.println ("Введите основное количество(целое число)");
		N = Integer.parseInt(in.readLine());

		System.out.println ("Введите базовую цену (2 цифры после запятой) (по умолчанию S/N)");
		tmp = in.readLine();
		if (!"".equals(tmp.trim())) {
			p1 = (int)(Double.parseDouble(tmp)*100);
		} 
		
		System.out.println ("Введите разброс цены (2 цифры после запятой) (по умолчанию 0.01)");
		tmp = in.readLine();
		if (!"".equals(tmp.trim())) {
			pc = (int)(Double.parseDouble(tmp)*100);
		} 
 
		//S = 2834989.06;
		//N = 142585;
		//S = 3986598.07;
		//N = 148963;
		
		
		Si = (int) (S * 100);
		if (p1==0)  p1 = (int) (Si / N);
		
		for (p2 = p1 + 1;p2 <= p1 + pc; p2++) { 
			for (q1 = N-1;q1>0;q1--) {
				q2=N-q1;
				Si1 = q1 * p1;
				Si2 = q2 * p2;
				S1 = Si1+Si2;
				if (S1 == Si)  
				System.out.println ("p1 = " + p1/100D + " p2 = " + p2/100D + " q1 = "+q1+" q2="+q2+" Si1="+Si1/100D+" Si2=" + Si2/100D + " S1="+S1/100D);
			}
		}

		System.out.println ("Конец работы");
		
		

	}

}
