package com.bage.study.java.set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  比较List、Map、Set
 * @author bage
 *
 */
public class ListSetMap {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		List<String> list = new ArrayList<String>(); // Collection
//		List：
//		  1.可以允许重复的对象。
//	　　       2.可以插入多个null元素。
//        3.是一个有序容器，保持了每个元素的插入顺序，输出的顺序就是插入的顺序。
//        4.常用的实现类有 ArrayList、LinkedList 和 Vector。
//        ArrayList 最为流行，它提供了使用索引的随意访问，而 LinkedList  则对于经常需要从 List 中添加或删除元素的场合更为合适。
	        
		Set<String> set = new HashSet<>();
		set = new TreeSet<>();
		set.add(null);
		System.out.println(set);
//		 Set：
//		 1.不允许重复对象
//	　　     2. 无序容器，你无法保证每个元素的存储顺序，TreeSet通过 Comparator  或者 Comparable 维护了一个排序顺序。
//       3. 只允许一个 null 元素
//       4.Set 接口最流行的几个实现类是 HashSet、LinkedHashSet 以及 TreeSet。最流行的是基于 HashMap 实现的 HashSet；TreeSet 还实现了 SortedSet 接口，因此 TreeSet 是一个根据其 compare() 和 compareTo() 的定义进行排序的有序容器。
		
		Map<String,String> map = new HashMap<String,String>();
		new HashMap<String,String>().put(null, null);
		new ConcurrentHashMap<>().put(null, null);
//		1.Map不是collection的子接口或者实现类。Map是一个接口。
//		2.Map 的 每个 Entry 都持有两个对象，也就是一个键一个值，Map 可能会持有相同的值对象但键对象必须是唯一的。
//		3. TreeMap 也通过 Comparator  或者 Comparable 维护了一个排序顺序。
//		4. Map 里你可以拥有随意个 null 值但最多只能有一个 null 键。
//		5.Map 接口最流行的几个实现类是 HashMap、LinkedHashMap、Hashtable 和 TreeMap。（HashMap、TreeMap最常用）
		
		
		Vector<String> vector ; // synchronized 线程安全
		
		
	}
	
}
