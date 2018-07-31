package com.bage.study.java.pattern.factory;

/**
 * 产品的工厂类
 * @author bage
 *
 */
public class ProductFactory {

	/**
	 * 产品类型
	 * @author bage
	 *
	 */
	enum ProductType{
		Phone,
		Computer
	}
	
	/**
	 * 根据不同类型获取产品
	 * @param type
	 * @return
	 */
	public static Product getProduct(ProductType type){
		Product product = null;
		switch (type) {
		case Phone:
			product = new Phone();
			break;
		case Computer:
			product = new Computer();
			break;
		default:
			break;
		}
		return product;
	}
}
