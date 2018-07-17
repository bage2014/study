package com.bage.study.java.multhread;

import java.util.concurrent.Semaphore;

import com.bage.study.java.multhread.officialdemo.Pool;

public class MySemaphore {

	public static void main(String[] args) {
		
		// defaultDemo();
		
		 // 信号量共10个
	    Semaphore semaphore = new Semaphore(10);
	    driveCar(semaphore);
	    
	}
	private static void defaultDemo() {
		try {
			final Pool pool = new Pool();
			System.out.println(pool.getItem());
			
			new Thread(){
				public void run() {
					try {
						Thread.sleep(5000);
						pool.putItem('1');
						System.out.println("pool.putItem('5');");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
			
			System.out.println(pool.getItem());
			System.out.println(pool.getItem());
			System.out.println(pool.getItem());
			
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 public static void driveCar(Semaphore semaphore) {
	        try {
	            System.out.println("Semaphore available permits: " + semaphore.availablePermits());
	            semaphore.acquire();
	            System.out.println("Semaphore available permits: " + semaphore.availablePermits());
	            semaphore.acquire(2);
	            System.out.println("Semaphore available permits: " + semaphore.availablePermits());
	            semaphore.acquire(3);
	            System.out.println("Semaphore available permits: " + semaphore.availablePermits());
	            semaphore.acquire(4);
	            System.out.println("Semaphore available permits: " + semaphore.availablePermits());
	            semaphore.release();
	            System.out.println("Semaphore available permits: " + semaphore.availablePermits());
	            semaphore.release(2);
	            System.out.println("Semaphore available permits: " + semaphore.availablePermits());
	            semaphore.release(3);
	            System.out.println("Semaphore available permits: " + semaphore.availablePermits());
	            semaphore.release(4);
	            System.out.println("Semaphore available permits: " + semaphore.availablePermits());
	            
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
}
