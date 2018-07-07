package com.bage.study.java.object;

/**
 * Object 学习<br>
 * 封装：属性私有、提供getset方法<br>
 * 继承：<br>
 * 多态：引用多态、方法多态<br>
 * 初始化顺序：static属性》属性》构造函数<br>
 * 
 * @author bage
 *
 */
public class MyObject implements Cloneable{

	private String uname = "哥哥";
	
	public static String uname3 = "哥哥3";

	
	private String pass;

	public MyObject(){
		System.out.println(uname);
	}
	
	public static void main(String[] args) throws Throwable {
		
		// 对象常用方法
		MyObject obj = new MyObject(); // 构造方法
		
		// 克隆
		obj.clone();  // CloneNotSupportedException - if the object's class does not support the Cloneable interface
		System.out.println(obj.hashCode() == obj.clone().hashCode());
		
		// 相等 == p (重写equals方法一般也会重写hashCode方法)
		System.out.println(obj.equals(obj.clone())); 
		// 因为 ：  if(equals == true){ hashCode ==true )
		
		System.out.println(obj.getClass()); // 获取class
		System.out.println(MyObject.class);

		 // 程序中调用无效？？
		obj.finalize();
		System.out.println(obj);
		
		// hashCode
		System.out.println(obj.hashCode());// 一般用于判断存在性第一步
		System.out.println(obj.hashCode());// 一般用于判断存在性第一步

		// toString
		System.out.println(obj.toString());
		//getClass().getName() + "@" + Integer.toHexString(hashCode())
		
		// 线程相关
		// obj.notify、wait等方法;
		
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
		result = prime * result + ((uname == null) ? 0 : uname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyObject other = (MyObject) obj;
		if (pass == null) {
			if (other.pass != null)
				return false;
		} else if (!pass.equals(other.pass))
			return false;
		if (uname == null) {
			if (other.uname != null)
				return false;
		} else if (!uname.equals(other.uname))
			return false;
		return true;
	}
	
	
	
}
