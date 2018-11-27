package com.bage.lifecircle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;

/**
 * BeanNameAware 让bean知道自己的name<br>
 * BeanFactoryAware BeanFactory拿到获取bean，来给当前bean使用<br>
 * ApplicationContextAware 拿到获取ApplicationContext，来给<br>
 * BeanPostProcessor
 * 
 * <code>
 * 
 BeanInit
setId
setName
setDesc
setBeanName:lifeCircleBean
beanFactory:org.springframework.beans.factory.support.DefaultListableBeanFactory@3407aa4f: defining beans [helloController,org.springframework.context.annotation.internalConfigurationAnnotationProcessor,org.springframework.context.annotation.internalAutowiredAnnotationProcessor,org.springframework.context.annotation.internalRequiredAnnotationProcessor,org.springframework.context.annotation.internalCommonAnnotationProcessor,org.springframework.context.event.internalEventListenerProcessor,org.springframework.context.event.internalEventListenerFactory,viewResolver,lifeCircleBean]; root of factory hierarchy
setApplicationContext:WebApplicationContext for namespace 'dispatcher-servlet': startup date [Mon Oct 29 21:35:16 CST 2018]; root of context hierarchy
afterPropertiesSet
init
beanName:org.springframework.context.event.internalEventListenerProcessor
postProcessBeforeInitialization:org.springframework.context.event.EventListenerMethodProcessor@384472bf
beanName:org.springframework.context.event.internalEventListenerProcessor

 * </code>
 * @author bage
 *
 */
@Scope
public class LifeCircleBean implements BeanNameAware,BeanFactoryAware,ApplicationContextAware,BeanPostProcessor,InitializingBean,DisposableBean{

	private String id;
	private String name;
	private String desc;
	
	public LifeCircleBean() {
		super();
		System.out.println("1构造函数");
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
		System.out.println("2设置属性");
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "BeanInit [id=" + id + ", name=" + name + ", desc=" + desc + "]";
	}

	/**
	 * Set the name of the bean in the bean factory that created this bean. 

	Invoked after population of normal bean properties 
	but before an init callback such as InitializingBean.afterPropertiesSet() or a custom init-method.

	 */
	public void setBeanName(String name) {
		System.out.println("3调用BeanNameAware的setBeanName方法");
	}

	/**
	 * Callback that supplies the owning factory to a bean instance. 

	Invoked after the population of normal bean properties 
	but before an initialization callback such as InitializingBean.afterPropertiesSet() or a custom init-method.

	 */
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("4调用BeanFactoryAware的setBeanFactory方法" + beanFactory);
	}

	/**
	 * Set the ApplicationContext that this object runs in. Normally this call will be used to initialize the object. 

	Invoked after population of normal bean properties 
	but before an init callback such as org.springframework.beans.factory.InitializingBean.afterPropertiesSet() 
	or a custom init-method. 
	Invoked after ResourceLoaderAware.setResourceLoader, ApplicationEventPublisherAware.setApplicationEventPublisher and MessageSourceAware, if applicable.

	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("5调用ApplicationContextAware的setApplicationContext方法" + applicationContext);
	}

	/**
	 * Apply this BeanPostProcessor to the given new bean instance before any bean initialization callbacks 
	 * (like InitializingBean's afterPropertiesSet or a custom init-method). 
	 * The bean will already be populated with property values. The returned bean instance may be a wrapper around the original.
	 */
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("6调用BeanPostProcessor的postProcessBeforeInitialization方法" + bean);
		return bean;
	}
	
	/**
	 * Invoked by a BeanFactory after it has set all bean properties supplied (and satisfied BeanFactoryAware and ApplicationContextAware). 

	This method allows the bean instance to perform initialization[ɪˌnɪʃəlaɪ'zeɪʃn] only possible 
	when all bean properties have been set 
	and to throw an exception in the event of misconfiguration.

	 */
	public void afterPropertiesSet() throws Exception {
		System.out.println("7调用InitializingBean的afterPropertiesSet方法");
	}
	
	/**
	 * 自定义初始化函数
	 */
	public void init() {
		System.out.println("8调用配置的init-method");
	}
	
	
	/**
	 * Apply this BeanPostProcessor to the given new bean instance after any bean initialization callbacks 
	 * (like InitializingBean's afterPropertiesSet or a custom init-method). 
	 * The bean will already be populated with property values. The returned bean instance may be a wrapper around the original. 

	In case of a FactoryBean, this callback will be invoked for both the FactoryBean instance and the objects created by the FactoryBean 
	(as of Spring 2.0). 
	The post-processor can decide whether to apply to either the FactoryBean or created objects or both through corresponding bean instanceof FactoryBean checks. 

	 */
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("9调用BeanPostProcessor的postProcessAfterInitialization方法" + bean);
		return bean;
	}

	/**
	 * 自定义销毁函数
	 */
	public void destroy() {
		System.out.println("10调用配置的destroy-method");
	}
}
