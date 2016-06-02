package com.akartkam.inShop.app;

import java.util.Random;

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
		for (int i = 0; i < 1000; i++)
		  System.out.println(new Random().nextInt(1000000000 - 100000000) + 100000000);
	}
}
