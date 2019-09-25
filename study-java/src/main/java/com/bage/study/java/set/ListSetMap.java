package com.bage.study.java.set;

import java.util.*;

/**
 *  比较List、Map、Set
 * @author bage
 *
 */
public class ListSetMap {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		List<String> list = new ArrayList<String>(); // Collection // 可以使用list迭代器listIterator
//		ListIterator<String> listIterator = list.listIterator();
//		listIterator.previous();
//		listIterator.remove();
		Vector<String> vector ; // synchronized 线程安全
		list = new LinkedList();
		
//		List：
//		  1.可以允许重复的对象。
//	　　       2.可以插入多个null元素。
//        3.是一个有序容器，保持了每个元素的插入顺序，输出的顺序就是插入的顺序。
//        4.常用的实现类有 ArrayList、LinkedList 和 Vector。
//        ArrayList 最为流行，它提供了使用索引的随意访问，而 LinkedList  则对于经常需要从 List 中添加或删除元素的场合更为合适。
	        
		Set<String> set = new HashSet<>();
		set = new HashSet<>();
		set.add(null); // TreeSet 不能放null
		System.out.println(set);
//		 Set：
//		 1.不允许重复对象
//	　　     2. 无序容器，你无法保证每个元素的存储顺序，TreeSet通过 Comparator  或者 Comparable 维护了一个排序顺序。
//       3. 只允许一个 null 元素
//       4.Set 接口最流行的几个实现类是 HashSet、LinkedHashSet 以及 TreeSet。最流行的是基于 HashMap 实现的 HashSet；TreeSet 还实现了 SortedSet 接口，因此 TreeSet 是一个根据其 compare() 和 compareTo() 的定义进行排序的有序容器。

		new TreeMap<>();

		Map<String,String> map = new HashMap<String,String>();
		new HashMap<String,String>().put(null, null);
		// 不允许放null：：new ConcurrentHashMap<>().put(null, null);
//		1.Map不是collection的子接口或者实现类。Map是一个接口。
//		2.Map 的 每个 Entry 都持有两个对象，也就是一个键一个值，Map 可能会持有相同的值对象但键对象必须是唯一的。
//		3. TreeMap 也通过 Comparator  或者 Comparable 维护了一个排序顺序。
//		4. Map 里你可以拥有随意个 null 值但最多只能有一个 null 键。
//		5.Map 接口最流行的几个实现类是 HashMap、LinkedHashMap、Hashtable 和 TreeMap。（HashMap、TreeMap最常用）
		
		
		
//		什么场景下使用list，set，map呢？
//		（或者会问为什么这里要用list、或者set、map，这里回答它们的优缺点就可以了）
//		答：
//		如果你经常会使用索引来对容器中的元素进行访问，那么 List 是你的正确的选择。如果你已经知道索引了的话，那么 List 的实现类比如 ArrayList 可以提供更快速的访问,如果经常添加删除元素的，那么肯定要选择LinkedList。
//		如果你想容器中的元素能够按照它们插入的次序进行有序存储，那么还是 List，因为 List 是一个有序容器，它按照插入顺序进行存储。
//		如果你想保证插入元素的唯一性，也就是你不想有重复值的出现，那么可以选择一个 Set 的实现类，比如 HashSet、LinkedHashSet 或者 TreeSet。所有 Set 的实现类都遵循了统一约束比如唯一性，而且还提供了额外的特性比如 TreeSet 还是一个 SortedSet，所有存储于 TreeSet 中的元素可以使用 Java 里的 Comparator 或者 Comparable 进行排序。LinkedHashSet 也按照元素的插入顺序对它们进行存储。
//		如果你以键和值的形式进行数据存储那么 Map 是你正确的选择。你可以根据你的后续需要从 Hashtable、HashMap、TreeMap 中进行选择。
		
		
		
//		线程安全集合类与非线程安全集合类 
//		LinkedList、ArrayList、HashSet是非线程安全的，Vector是线程安全的;
//		HashMap是非线程安全的，HashTable是线程安全的;
//		StringBuilder是非线程安全的，StringBuffer是线程安全的。
		
	}
	
}
