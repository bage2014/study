/**
 * 
 */
package com.gootrip.util;

/**
 * @author Administrator
 *
 */
public class DebugOut {
private static boolean DEBUG = true;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static void print(String arg0){
		if (DEBUG)
			System.out.print(arg0);
	}
	public static void println(String arg0){
		if (DEBUG)
			System.out.println(arg0);
	}

}
