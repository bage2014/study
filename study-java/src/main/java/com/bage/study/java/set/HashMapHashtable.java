package com.bage.study.java.set;

/**
 * HashMap和Hashtable区别
 * @author bage
 *
 */
public class HashMapHashtable {

	public static void main(String[] args) {
		
//		 1、继承的父类不同
//	      Hashtable继承自Dictionary类，而HashMap继承自AbstractMap类。但二者都实现了Map接口。
//
//	      2、线程安全性不同
//	      
//
//	      3、是否提供contains方法
//	      HashMap把Hashtable的contains方法去掉了，改成containsValue和containsKey，因为contains方法容易让人引起误解。
//	      Hashtable则保留了contains，containsValue和containsKey三个方法，其中contains和containsValue功能相同。
	      
//		4、key和value是否允许null值
//	      其中key和value都是对象，并且不能包含重复key，但可以包含重复的value。
//	      Hashtable中，key和value都不允许出现null值。但是如果在Hashtable中有类似put(null,null)的操作，编译同样可以通过，因为key和value都是Object类型，但运行时会抛出NullPointerException异常，这是JDK的规范规定的。
//	HashMap中，null可以作为键，这样的键只有一个；可以有一个或多个键所对应的值为null。当get()方法返回null值时，可能是 HashMap中没有该键，也可能使该键所对应的值为null。因此，在HashMap中不能由get()方法来判断HashMap中是否存在某个键， 而应该用containsKey()方法来判断。
	
//	      5、两个遍历方式的内部实现上不同
//	      Hashtable、HashMap都使用了 Iterator。而由于历史原因，Hashtable还使用了Enumeration的方式 。
	      
//		 6、hash值不同
//	      哈希值的使用不同，HashTable直接使用对象的hashCode。而HashMap重新计算hash值。
	
//		  7、内部实现使用的数组初始化和扩容方式不同
//	      HashTable在不指定容量的情况下的默认容量为11，而HashMap为16，Hashtable不要求底层数组的容量一定要为2的整数次幂，而HashMap则要求一定为2的整数次幂。
//	      Hashtable扩容时，将容量变为原来的2倍加1，而HashMap扩容时，将容量变为原来的2倍
	      
	}
	
}
