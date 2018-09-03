package com.bage.study.guava.avoidnull;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Optional;

public class OptionalTest {

	// 基本用法
	@Test
	public void usage() {
		Optional<Integer> possible = Optional.of(5);// Optional.of(null) 报错;
		assertTrue(possible.isPresent()); // returns true
		assertTrue(5 == possible.get());
	}
	
	// of null
	@Test(expected = NullPointerException.class)
	public void of() {
		Optional.of(null); // 抛异常
	}
	
	// fromNullable
	@Test(expected = IllegalStateException.class)
	public void fromNullable() {
		Optional<Object> possible = Optional.fromNullable(null); // fromNullable 可以为null
		assertTrue(false == possible.isPresent());
		possible.get(); // 	Returns the contained T instance, which must be present; otherwise, throws an IllegalStateException.
		assertEquals(2,possible.or(2));
		assertTrue( null == possible.orNull());
	}
		
}
