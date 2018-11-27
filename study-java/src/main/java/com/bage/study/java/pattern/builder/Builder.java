package com.bage.study.java.pattern.builder;

/**
 * 构造器接口
 * @author bage
 *
 */
public interface Builder {

	public Builder setPart1(Part1 part1);
	public Builder setPart2(Part2 part2);
	public Builder setPart3(Part3 part3);
	
	public Product build();
	
}
