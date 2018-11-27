package com.bage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;

public class MyContextLoaderListener {

	public static void main(String[] args) {
		
		String beansFilePathStr[] = {
				"classpath:template.xml"  // 包下的配置文件
		};
		
		// 加载 spring 容器
		ApplicationContext context =
		        new ClassPathXmlApplicationContext(beansFilePathStr); 
//		ConfigurableApplicationContext context =
//				new ClassPathXmlApplicationContext(beansFilePathStr); //Shutting down the Spring IoC container gracefully in non-web applications
		// beans.xml == classpath:beans.xml 等价
		// add a shutdown hook for the above context...Shutting down the Spring IoC container gracefully in non-web applications
		// context.registerShutdownHook();
        
		// 获取一个基本bean
		MyBean myBean = context.getBean("myBean",MyBean.class);
		System.out.println(myBean);
		
		ContextLoaderListener c; //用于CTRL 查看源码
		DispatcherServlet d;//用于CTRL 查看源码
		
		ApplicationContext ApplicationContext = new ClassPathXmlApplicationContext();

		//ApplicationContext.getBean(requiredType);
		
	}
}
