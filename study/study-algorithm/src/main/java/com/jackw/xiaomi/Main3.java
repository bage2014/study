package com.jackw.xiaomi;

import java.util.Random;

public class Main3 {
	public static synchronized void main(String[] args) {
		Thread t = new Thread() {
			@Override
			public void run() {
				hello();
			}
		};
		t.start();

		try {
			Thread.sleep(new Random().nextInt(500) +2000);
		} catch (InterruptedException e) {

		}
		System.out.print("There");
	}

	static synchronized void hello() {
		try {
			Thread.sleep(new Random().nextInt(500));
		} catch (InterruptedException e) {

		}
		System.out.print("Hello");
	}
}