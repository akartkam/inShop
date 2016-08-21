package com.akartkam.inShop.app;

import com.akartkam.inShop.util.Random10OrderNumberGeneratorImpl;

public class GenerateRandom {
	public static void main(String[] args) {
    /*	Random rand = new Random();

		for (int i = 0; i < 10; i++) {
			System.out.println(String.format("%b\t %d\t %d\t %d\t %.10f\t %.10f", rand.nextBoolean(), rand.nextInt(),
					rand.nextInt(20), rand.nextLong(), rand.nextFloat(), rand.nextDouble()));
		}

		System.out.println("\nFixed seed");
		for (int i = 0; i < 10; i++) {
			if (i % 3 == 0) {
				rand.setSeed(2L);
			}
			System.out.println(String.format("%b\t %d\t %d\t %d\t %.10f\t %.10f", rand.nextBoolean(), rand.nextInt(),
					rand.nextInt(20), rand.nextLong(), rand.nextFloat(), rand.nextDouble()));
		}*/
		Random10OrderNumberGeneratorImpl r = new Random10OrderNumberGeneratorImpl();
		r.setPrefix("T-");
		System.out.println(r.generateOrderNumber(null));
		//for (int i = 0; i < 1000; i++) {
		  //System.out.print(new Random().nextInt(999999999)+100000000L);
		  //System.out.print("--");
		 // Long l = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
		 // String s = String.format("%d", l);
		 // System.out.println(s);
		//}
	}
}
