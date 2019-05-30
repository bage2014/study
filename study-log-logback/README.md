# study-log-logback #
logback 基本使用


## 参考链接 ##

- 中文官网 [http://www.logback.cn/](http://www.logback.cn/)
- demo [http://github.com/qos-ch/logback-demo](http://github.com/qos-ch/logback-demo)
- How to setup SLF4J and LOGBack in a web app - fast [https://wiki.base22.com/btg/how-to-setup-slf4j-and-logback-in-a-web-app-fast-35488048.html](https://wiki.base22.com/btg/how-to-setup-slf4j-and-logback-in-a-web-app-fast-35488048.html)

## 环境搭建 ##
添加logback依赖

	<dependency>
		<groupId>ch.qos.logback</groupId>
		<artifactId>logback-classic</artifactId>
		<version>1.0.13</version>
	</dependency>

导入xml配置文件

logback.xml [https://github.com/bage2014/study/blob/master/study-log-logback/src/main/resources/logback.xml](https://github.com/bage2014/study/blob/master/study-log-logback/src/main/resources/logback.xml)、[logback.xml](https://wiki.base22.com/btg/files/35488048/35618832/1/1261291177000/logback.xml)

logback-test.xml [https://github.com/bage2014/study/blob/master/study-log-logback/src/main/resources/logback-test.xml](https://github.com/bage2014/study/blob/master/study-log-logback/src/main/resources/logback-test.xml)、[https://wiki.base22.com/btg/files/35488048/35618833/1/1261291177000/logback-test.xml](https://wiki.base22.com/btg/files/35488048/35618833/1/1261291177000/logback-test.xml)

	src
		main
			resources
				logback.xml
		test
			resources
				logback-test.xml

示例代码

	package com.bage.study.log.logback;
	
	// Add the following to the imports section of your java code:
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	
	/**
	 * LogApp
	 */
	public class LogApp {
	
		// Add the following at the top of your class in the global section (just under the line that declares your class public class Whatever extends Whatever). Change the name of the class (MyClassName) in the getLogger method call, of course. Name it the same as the class you're dropping this code into.
		private static final Logger LOG = LoggerFactory.getLogger(LogApp.class);
	
		public static void main(String[] args) {
			// Throw some logging statements in your code somewhere where you know they'll be fired right away when you run your app. For example:
			LOG.trace("Hello World!");
			LOG.debug("How are you today?");
			LOG.info("I am fine.");
			LOG.warn("I love programming.");
			LOG.error("I am programming.");
			
		}
	}


运行LogApp类即可
查看控制台输出

## 环境搭建 ##
