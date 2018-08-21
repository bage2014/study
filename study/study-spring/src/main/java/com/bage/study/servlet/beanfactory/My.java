package com.bage.study.servlet.beanfactory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class My {

	public static void main(String[] args) {
		
		BeanFactory factory = new DefaultListableBeanFactory(); // 接口
		// 延迟加载形式来注入Bean
		
		
		ApplicationContext context = new ClassPathXmlApplicationContext();
		// context.publishEvent(null);
		
	}
	
}
