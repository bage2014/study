package com.bage.study.call.trace.asm;

import java.util.Arrays;

public class ASMTest 
{
	
	private static StringBuilder sb;
	
	private static final String hello = "Hello World!";
	
    public static void main( String[] args )
    {
    	int len = args.length;
    	ASMTest test = new ASMTest();
        System.out.println(hello);
        int i = test.fac(10);
        System.out.println(i);
        System.out.println("done");
        System.out.println(sb);
        double d = test.add(1,2.);
        System.out.println(d);
        int[] arr = {1,2,3};
        i = test.sum(arr);
        i = test.sum2(arr);
        Arrays.stream(arr).reduce((a,b)->a+b).ifPresent(System.out::println);
        double[] arr2 = {1.0, 2.0, 3.0};
        d = test.sum(arr2);
        System.out.println(i);
        System.out.println(len);
    }
    
    public int add(int a, int b) {
    	return a+b;
    }
    
    public double add(int a, double b) {
    	return a+b;
    }
    
    public int sum(int[] arr) {
    	int sum = 0;
    	for(int i = 0; i < arr.length; i ++) {
    		sum += arr[i];
    	}
    	return sum;
    }
    
    public int sum2(int[] arr) {
    	int sum = 0;
    	for(int i : arr) {
    		sum += i;
    	}
    	return sum;
    }
    
    public double sum(double[] arr) {
    	double sum = 0;
    	for(int i = 0; i < arr.length; i ++) {
    		sum += arr[i];
    	}
    	return sum;
    }
    
    public int fac(int a) {
    	if (sb == null) {
    		sb = new StringBuilder();
    	}
    	if (a == 1) {
    		sb.append("" + 1 + ".");
    		return 1;
    	} else {
    		int intermediary = a * fac(a-1);
    		sb.append("" + intermediary + ".");
    		return intermediary;
    	}
    }
}
