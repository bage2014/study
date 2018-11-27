package com.bage.study.java.string;

import java.util.LinkedList;
import java.util.List;

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
		
		temp = "123";
		temp2 = "1" + "23";
		
		Integer vc1 = 13;
//		if (i >= IntegerCache.low && i <= IntegerCache.high)
//        	return IntegerCache.cache[i + (-IntegerCache.low)];
		Integer vc2 = Integer.valueOf(13);
		Integer vc3 = new Integer(13);
		System.out.println(vc1 == vc2);
		System.out.println(vc1 == vc3);
		
		
		a = "hello";
        String bb =  new String("hello");
        String c =  new String("hello");
        String d = bb.intern();
         
        System.out.println("-----------------");
        System.out.println(a==bb);
        System.out.println(bb==c);
        System.out.println(bb==d);
        System.out.println(a==d);
        System.out.println("-----------------");

        a = "hello";
        bb =  new String(a);
        System.out.println(a==bb);
        
        
        String aaa = "aa";
        String bbb = String.valueOf("aa");
        System.out.println(aaa == bbb);
        
        // 测试split性能
        long bf = System.nanoTime();
        String toSplitStr = initString(1000,",");
        toSplitStr.split(",");
        System.out.println("耗时：" + (System.nanoTime() - bf));
        
        bf = System.nanoTime();
        mySplit(toSplitStr,",");
        System.out.println("耗时：" + (System.nanoTime() - bf));
        
        
        
	}

	private static List<String> mySplit(String toSplitStr, String ch) {
		List<String> list = new LinkedList<String>();
	    while(true){
	        int index = toSplitStr.indexOf(ch);    //找分隔符的位置
	        if(index < 0){
	        	break;             //没有分隔符存在，break
	        }
	        list.add(toSplitStr.substring(0,index));
	        toSplitStr = toSplitStr.substring(index+1);       //剩下需要处理的字符串
	    }
	    System.out.println(list.size());
	    return list;
	}

	private static String initString(int n, String ch) {
		StringBuilder sb=new StringBuilder();
		for(int i =0 ; i < n; i ++){
		    sb.append(i).append(ch);
		}
		return sb.toString();
	}
}
