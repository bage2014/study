package com.bage.study.guava.precondition;

import org.junit.Test;

import com.google.common.base.Preconditions;

/**
 * 
 * @author luruihua
 *
 */
public class PreconditionTest {

	@Test(expected=IllegalArgumentException.class)
	public void checkArgument() {
		int i = 1;
		int j = 0;
		Preconditions.checkArgument(i >= 0, "Argument was %s but expected nonnegative", i);
		Preconditions.checkArgument(i < j, "Expected i < j, but %s >= %s", i, j);
	}
	
	@Test(expected=NullPointerException.class)
	public void checkNotNull() {
		Preconditions.checkNotNull(null);
	}
	
	@Test(expected=IllegalStateException.class)
	public void checkState() {
		Preconditions.checkState(1 > 2);
	}
	
	@Test
	public void checkElementIndex() {
		int index = 1;
		int size = 10;
		Preconditions.checkElementIndex(index, size);
		Preconditions.checkPositionIndex(index, size);
		
	}
	
	
}
