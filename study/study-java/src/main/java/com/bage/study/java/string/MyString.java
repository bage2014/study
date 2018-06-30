package com.bage.study.java.string;

public class MyString {

	public static void main(String[] args) {
		
		
		String a = "ad"; // 定义
		//a = a.concat("da");
		a = a + "da";
		
		System.out.println(a);
		
		
		float floatVar = 1;
		int intVar = 2;
		String stringVar = "dsds";
		String str;
		
		/// 格式化
		str = String.format("浮点型变量的值为 " +
		                   "%f, 整型变量的值为 " +
		                   " %d, 字符串变量的值为 " +
		                   " %s", floatVar, intVar, stringVar);
		System.out.println(str);
		
		// String.format(format, args); // 参考链接：https://blog.csdn.net/qq_25925973/article/details/54407994
		// %[argument_index$][flags][width][.precision]conversion
//		argument_index: 可选,是一个十进制整数，用于表明参数在参数列表中的位置。第一个参数由 "1$" 引用，第二个参数由 "2$" 引用，依此类推。
//
//		flags: 可选,用来控制输出格式
//
//		width: 可选,是一个正整数，表示输出的最小长度
//
//		precision:可选,用来限定输出字符数
//
//		conversion:必须,用来表示如何格式化参数的字符
		System.out.println(String.format("我叫%s,她叫%s", "小明","小方") );// 我叫小明,她叫小方
		System.out.println(String.format("我叫%2$s,她叫%1$s", "小明","小方")); // 我叫小方,她叫小明 
		System.out.println(String.format("%1$,d", 12302562)); // 12,302,562  
		System.out.println(String.format("%1$08d", 123456));// 00123456 
		System.out.println(String.format("%1$.2f", 12.12555));// 12.13  
		
		// 相等
		a = "bas";
		StringBuffer b = new StringBuffer("bas");
		System.out.println(a.contentEquals(b)); // contentEquals 不一定是同类型
		System.out.println(a.equals(b));
		
		// 构建对象
		String temp = "123";
		String temp2 = "123";
		String temp3 = new String("123");
		System.out.println(temp == temp2);
		System.out.println(temp.equals(temp2));
		System.out.println(temp == temp3);
		temp3 = "123".intern();
		System.out.println(temp == temp3);

		
		// join
		System.out.println(String.join(",", "dsd","dsd","dsd"));
		
		
		
	}
}
