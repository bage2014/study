package com.bage.study.java.multhread;

public class Volatile {

	private static int i = 0;
	
	public static void count(){
		i ++;
	}
	
	public static void main(String[] args) {

		new Thread(){
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(i >= 4){
						break;
					}
					count();
					//System.out.println(i);
				}
			};
		}.start();
		System.out.println("Thread:" + i);
		new Thread(){
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(i >= 4){
						break;
					}
					count();
					//System.out.println(i);
				}
			};
		}.start();
		System.out.println("Thread:" + i);
		new Thread(){
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(i >= 4){
						break;
					}
					count();
					//System.out.println(i);
				}
			};
		}.start();
		System.out.println("Thread:" + i);
		new Thread(){
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(i >= 4){
						break;
					}
					count();
					//System.out.println(i);
				}
			};
		}.start();
		System.out.println("Thread:" + i);
		new Thread(){
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(i >= 4){
						break;
					}
					count();
					//System.out.println(i);
				}
			};
		}.start();
		System.out.println("Thread:" + i);

		while(true){
			if(i >= 4){
				System.out.println("\n" + i);
				break;
			}
			System.out.print(".");
		}
		
	}
}
