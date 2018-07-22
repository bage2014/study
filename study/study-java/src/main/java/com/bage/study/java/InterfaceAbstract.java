package com.bage.study.java;

/**
 * 抽象类和接口[内部类待定]
 * @author bage
 *
 */
public class InterfaceAbstract {

	public static void main(String[] args) {
		
		int n = IAB.field;
		//B.a = 10;
	}
	
}

interface IAB /*extends IAC 不可以继承类 */{
	int field = 0; // 只能是static final 类型
	int getII();
}

abstract class IAC/* implements IAB*/{
	
	public IAC(){
		
	}
	
	//abstract int getI();
}

class IAD implements IAB{

	@Override
	public int getII() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}