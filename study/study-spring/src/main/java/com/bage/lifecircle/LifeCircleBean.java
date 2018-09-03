package com.bage.lifecircle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;

/**
 * BeanNameAware 让bean知道自己的name<br>
 * BeanFactoryAware BeanFactory拿到获取bean，来给当前bean使用<br>
 * ApplicationContextAware 拿到获取ApplicationContext，来给<br>
 * BeanPostProcessor
 * @author bage
 *
 */
@Scope
public class LifeCircleBean implements BeanNameAware,BeanFactoryAware,ApplicationContextAware,BeanPostProcessor{

	private String id;
	private String name;
	private String desc;
	
	public LifeCircleBean() {
		super();
		System.out.println("BeanInit");
	}
	
	public LifeCircleBean(String id, String name, String desc) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		System.out.println("setId");
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		System.out.println("setName");
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		System.out.println("setDesc");
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "BeanInit [id=" + id + ", name=" + name + ", desc=" + desc + "]";
	}

	public void setBeanName(String name) {
		System.out.println("setBeanName:" + name);
	}

	public void init() {
		System.out.println("init");
	}

	public void destroy() {
		System.out.println("destroy");
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("beanFactory:" + beanFactory);
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("setApplicationContext:" + applicationContext);
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("postProcessBeforeInitialization:" + bean);
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("postProcessAfterInitialization:" + bean);
		return bean;
	}

	
}
