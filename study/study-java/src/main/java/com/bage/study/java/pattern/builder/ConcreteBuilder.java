package com.bage.study.java.pattern.builder;

/**
 * 具体的构造器
 * @author bage
 *
 */
public class ConcreteBuilder implements Builder{

	Product product = new Product();
	
	@Override
	public Product build() {
		return product;
	}

	@Override
	public Builder setPart1(Part1 part1) {
		product.setPart1(part1);
		return this;
	}

	@Override
	public Builder setPart2(Part2 part2) {
		product.setPart2(part2);
		return this;
	}

	@Override
	public Builder setPart3(Part3 part3) {
		product.setPart3(part3);
		return this;
	}

}
