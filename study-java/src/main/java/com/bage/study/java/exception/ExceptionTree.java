package com.bage.study.java.exception;

/**
 * 异常集成关系<br>
 * -- Throwable<br>
			-- error // 系统、jvm，未检查异常（unchecked）<br>
			-- exception // 程序<br>
				-- runtimeexception // 未检查异常（unchecked）<br>
					-- NullPointerException 空指针异常<br>
					-- ArrayIndexOutOfBoundsException 数组下标越界异常<br>
					-- ArithmeticException // Thrown when an exceptional arithmetic condition has occurred. For example, an integer "divide by zero"<br>   
					-- ClassCastException 类型转换异常<br>
					-- IndexOutOfBoundsException<br>
					-- NumberFormatException<br>
			-- stackrecorder<br>
 * @author bage
 *
 */
public class ExceptionTree {

	/**
	 * ThrowAble
	 * @param args
	 */
	public static void main(String[] args) {
	/*	
		-- Throwable
			-- error // 系统、jvm，未检查异常（unchecked）
			-- exception // 程序
				-- runtimeexception // 未检查异常（unchecked）
					-- NullPointerException 空指针异常
					-- ArrayIndexOutOfBoundsException 数组下标越界异常
					-- ArithmeticException // Thrown when an exceptional arithmetic condition has occurred. For example, an integer "divide by zero"
					-- ClassCastException 类型转换异常
					-- IndexOutOfBoundsException
					-- NumberFormatException
			-- stackrecorder
		*/
		Exception eception = new Exception();
		eception.printStackTrace();
		
		eception = new RuntimeException();
		
		RuntimeException a = new NullPointerException();
		 a = new IndexOutOfBoundsException();
		 a = new ArrayIndexOutOfBoundsException();
		 a = new ArithmeticException();
		 a = new ClassCastException();
		 IllegalArgumentException b = new NumberFormatException(); // IllegalArgumentException
		 a = b;
		 
		 System.out.println(a);
	}
	
}
