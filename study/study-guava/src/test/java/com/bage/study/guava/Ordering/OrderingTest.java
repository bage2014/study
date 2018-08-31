package com.bage.study.guava.Ordering;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.junit.Test;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

public class OrderingTest {

	@Test
	public void usage() {
		List<String> list = new ArrayList<String>();
		list.add("aaaa");
		list.add("bv");
		list.add("e");
		list.add("ddd");
		list.add("cqqqqq");
		
		// 根据长度排序
		Ordering<String> byLengthOrdering = new Ordering<String>() {
			  public int compare(String left, String right) {
			    return Ints.compare(left.length(), right.length());
			  }
			};
		list = byLengthOrdering
				.reverse()
				.sortedCopy(list);
		System.out.println(list);
		
		assertTrue(Ordering.natural()
				// .reverse()
				.isOrdered(list ));
	}
	
	@Test
	public void natural() {
		List<String> list = new ArrayList<String>();
		list.add("aaaa");
		list.add("bv");
		list.add("e");
		list.add("ddd");
		list.add("cqqqqq");
		
		list = Ordering
				.natural()
				// .reverse()
				.sortedCopy(list); // 基本排序
		System.out.println(list);
		
		assertTrue(Ordering.natural()
				// .reverse()
				.isOrdered(list ));
	}
	
	
	@Test
	public void onResultOf() {
		List<String> list = new ArrayList<String>();
		list.add("aaaa");
		list.add("bv");
		list.add("e");
		list.add("ddd");
		list.add("cqqqqq");
		
		Ordering<Foo> ordering = Ordering.natural().nullsFirst().onResultOf(new Function<Foo, String>() {
			  public String apply(Foo foo) {
			    return foo.sortedBy;
			  }
			});
		System.out.println(list);
		
		assertTrue(Ordering.natural()
				// .reverse()
				.isOrdered(list ));
	}
	
	
}
class Foo {
  @Nullable String sortedBy;
  int notSortedBy;
}