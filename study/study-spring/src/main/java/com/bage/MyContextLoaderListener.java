package com.bage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;

public class MyContextLoaderListener {

	public static void main(String[] args) {
		
		ContextLoaderListener c; //用于CTRL 查看源码
		DispatcherServlet d;//用于CTRL 查看源码
		
		ApplicationContext ApplicationContext = new ClassPathXmlApplicationContext();

		//ApplicationContext.getBean(requiredType);
	}
}
