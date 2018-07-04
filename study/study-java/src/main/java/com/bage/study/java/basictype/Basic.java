package com.bage.study.java.basictype;

/**
 * 基本数据类型<br>
 * 1字节   	-2^7~-2^7
 * 
 * @author bage
 *
 */
public class Basic {

	public static void main(String[] args) {
		
		byte i = 127; // 1字节  
    	short j = 1; // 2字节  
    	int a = 2; // 4字节
    	long g = 2; // 8字节
    	
    	boolean s = false; // 1字节 0/1(只有0或者1)
    	char h = 1;// 2字节  
    	float b = 2; // 4字节
    	double f = 3;// 8字节
		
    	
    	Byte ii = 127; // 1字节   // -128~127 全部缓存
    	Short jj = 1; // 2字节   // -128~127 缓存
    	Integer aa = 2; // 4字节
    	Long gg = 2L; // 8字节
    	
    	Boolean ss = false; // 1字节 0/1(只有0或者1)
    	Character hh = 1;// 2字节  
    	Float bb = 2f; // 4字节
    	Double ff = 3d;// 8字节
		
    	//System.out.println(127f == 127d);
    	System.out.println(0.1d); // 11111110111001100110011001100110011001100110011001100110011010
    	System.out.println(0.1f); // 111101110011001100110011001101
    							  // 1111111011100110011001100110011010000000000000000000000000000
    	System.out.println(0.1d == 0.1f);
    	System.out.println(i);

    	
    	double d1 = 0.1, d2 = 0.1; 
    	if(d1 == d2) {System.out.println("坏代码 ");}// 坏代码 
    	if(Double.compare(d1, d2) == 0) {System.out.println("好代码 ");}// 好代码 
    	
    	
    	System.out.println(Byte.SIZE);

    	
    	
	}
	
}
