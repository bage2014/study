package com.bage.study.java.multhread;

import java.util.Random;
import java.util.concurrent.Exchanger;

/**
 * Exchanger 基本使用
 * @author bage
 *
 */
public class MyExchanger {

	public static void main(String[] args) {
		
		Exchanger<Object> Exchanger = new Exchanger<Object>();
		
		MyExchangerThread exchangerThread1 = new MyExchangerThread("1","data1",Exchanger);
		MyExchangerThread exchangerThread2 = new MyExchangerThread("2","data2",Exchanger);
		
		exchangerThread1.start();
		exchangerThread2.start();
		
	}
	
}

class MyExchangerThread extends Thread{
	
	private Exchanger<Object> exchanger;
	private Object data;
	private String name;
	
	public MyExchangerThread(String name, Object data,Exchanger<Object> exchanger){
		this.name = name;
		this.data = data;
		this.exchanger = exchanger;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("thread "+name+" 开始运行" + System.currentTimeMillis());
			Thread.sleep(new Random().nextInt(5) * 1000);
			System.out.println("thread "+name+" 开始交换" + System.currentTimeMillis());
			System.out.println("thread "+name+" data前:" + data);
			data = exchanger.exchange(data);
			System.out.println("thread "+name+" data后:" + data);
			System.out.println("thread "+name+" 交换结束" + System.currentTimeMillis());
			Thread.sleep(new Random().nextInt(5) * 1000);
			System.out.println("thread "+name+" 运行结束" + System.currentTimeMillis());
		} catch (Exception e) {
		}
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
}