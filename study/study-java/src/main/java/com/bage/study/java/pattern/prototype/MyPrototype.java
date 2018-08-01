package com.bage.study.java.pattern.prototype;

import java.util.Date;

/**
 * 原型模式（克隆效率远比new高）<br>
 * 与单例模式相对<br>
 * 浅拷贝、深拷贝、序列化反序列化<br>
 * 原型模式就是从一个对象再创建另外一个可定制的对象，而且不需要知道任何创建的细节。<br>
 * 隐藏了制造新实例的复杂性，使得创建对象就像我们在编辑文档时的复制粘贴一样简单。<br>
 * 使用原型模式时不能有final对象。<br>
 * @author bage
 *
 */
public class MyPrototype {

	public static void main(String[] args) throws Exception {
		// 原型模式(克隆需要重写 clone 方法，深克隆需要克隆对象属性[非final])
		Prototype p = new Prototype(0,"bage",new ObjectAttr("value"));
		System.out.println(p);
		System.out.println(p.clone()); // 相同属性的不同对象
		System.out.println(p.serializable()); // 相同属性的不同对象
		
		// JDK clone实现demo java.util.Date
		Date date = new Date();
		System.out.println(date);
		System.out.println(date.clone());
		
	}
	
}
