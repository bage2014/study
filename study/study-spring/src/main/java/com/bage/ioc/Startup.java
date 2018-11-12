package com.bage.ioc;

import java.util.Set;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

/**
 * 启动过程，核心方法调用【不具备调试启动功能】
 * 
 * @author bage
 *
 */
public class Startup {

	public static void main(String[] args) throws Exception {

		// 1、从指定的 location 中加载配置好的Bean对象到actualResources资源中
		loadBeanDefinitions(null, null);
		// 2、由Resource中转化成InputSource
		// org.springframework.beans.factory.xml.XmlBeanDefinitionReader.loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException
		// 3、loadDocument
		loadDocument(null, null, null, 0, true);
		// 4、
		decorateBeanDefinitionIfRequired(null, null, null);
		// 5 TODO
		
	}

	/**
	 * 从指定的 location 中加载配置好的Bean对象到actualResources资源中
	 * 
	 * @param location
	 *            文件路径，支持正则表达式
	 * @param actualResources
	 *            加载至此资源中
	 */
	@SuppressWarnings("null")
	static void loadBeanDefinitions(String location, Set<Resource> actualResources)
			throws BeanDefinitionStoreException {
		org.springframework.beans.factory.support.AbstractBeanDefinitionReader reader = null;
		
		reader.loadBeanDefinitions(location, actualResources);

	}

	
	/**
	 * 将inputSource输入流加载到document中并返回
	 * 
	 * @param inputSource 待加载资源
	 * @param entityResolver 实体解析器
	 * @param errorHandler 错误异常处理器
	 * @param validationMode  验证模式：无 + XSD + DTD
	 * @param namespaceAware 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	static Document loadDocument(InputSource inputSource, EntityResolver entityResolver, ErrorHandler errorHandler,
			int validationMode, boolean namespaceAware) throws Exception {
		org.springframework.beans.factory.xml.DefaultDocumentLoader loader = null;
		return loader.loadDocument(inputSource, entityResolver, errorHandler, validationMode, namespaceAware);
	}
	
	/**
	 * 
	 * @param ele
	 * @param definitionHolder
	 * @param containingBd
	 * @return
	 */
	@SuppressWarnings("null")
	static BeanDefinitionHolder decorateBeanDefinitionIfRequired(Element ele, BeanDefinitionHolder definitionHolder, BeanDefinition containingBd){
		org.springframework.beans.factory.xml.BeanDefinitionParserDelegate delegate = null;
		return delegate.decorateBeanDefinitionIfRequired(ele, definitionHolder, containingBd);
	}
	
	/**
	 * 注册一个bean
	 * @param beanName bean名称
	 * @param beanDefinition bean的定义[bean数据结构]
	 * @throws BeanDefinitionStoreException
	 */
	@SuppressWarnings("null")
	static void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException{
		org.springframework.beans.factory.support.DefaultListableBeanFactory factory = null;
		factory.registerBeanDefinition(beanName, beanDefinition);
	}
}
