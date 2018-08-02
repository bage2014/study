package com.bage.study.java.pattern.factory.simple;

import com.bage.study.java.pattern.factory.Computer;
import com.bage.study.java.pattern.factory.Phone;
import com.bage.study.java.pattern.factory.Product;

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
	public enum ProductType{
		Phone,
		Computer
	}
	
	/**
	 * 根据不同类型获取产品(目前实现为枚举，也可以使用if，也可以使用反射)
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
	

	public static Product getProductPhone(){
		return new Phone();
	}
	
	public static Product getProductComputer(){
		return new Computer();
	}
	
}
