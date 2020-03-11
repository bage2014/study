package com.bage.study.java.basictype;

import java.math.BigDecimal;

public class BigDecimalTest {

	public static void main(String[] args) {

		// 不能使用如下方式 new
    	BigDecimal bigDecimal1 = new BigDecimal(1.00002);
    	BigDecimal bigDecimal2 = new BigDecimal(0.00008);

		System.out.println(bigDecimal1.add(bigDecimal2));
		// 结果为 ： 1.000099999999999908986019793444288694672650308348238468170166015625

		// 必须使用如下方式 new BigDecimal("");
		bigDecimal1 = new BigDecimal("1.00002");
		bigDecimal2 = new BigDecimal("0.00008");

		System.out.println(bigDecimal1.add(bigDecimal2)); // 与原来相同位数
    	//  结果为 ： 1.00010

		// 或使用如下方式 BigDecimal.valueOf(xxx);
		bigDecimal1 = BigDecimal.valueOf(0.00008);
		bigDecimal2 = BigDecimal.valueOf(1.00002);

		System.out.println(bigDecimal1.add(bigDecimal2)); // 会多出来一个位数
		//  结果为 ： 1.00010


		// BigDecimal 缓存的值 0 到 10 ,跟 Integer 缓存 -028 到 127 有点像
		System.out.println(BigDecimal.valueOf(1) == BigDecimal.valueOf(1));
		System.out.println(BigDecimal.valueOf(11) == BigDecimal.valueOf(11));
	}
	
}
