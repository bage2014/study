package com.bage.study.log.logback;

// Add the following to the imports section of your java code:
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LogAppTest
 */
public class LogAppTest {

	// Add the following at the top of your class in the global section (just under the line that declares your class public class Whatever extends Whatever). Change the name of the class (MyClassName) in the getLogger method call, of course. Name it the same as the class you're dropping this code into.
	private static final Logger LOG = LoggerFactory.getLogger(LogAppTest.class);

	@org.junit.Test
	public void test() {
		// Throw some logging statements in your code somewhere where you know they'll be fired right away when you run your app. For example:
		LOG.trace("Hello World!");
		LOG.debug("How are you today?");
		LOG.info("I am fine.");
		LOG.warn("I love programming.");
		LOG.error("I am programming.");
		
	}
	@org.junit.Test
	public void testMasking() {
		// Throw some logging statements in your code somewhere where you know they'll be fired right away when you run your app. For example:
		String json = "{\n" +
				"    \"user_id\":\"87656\",\n" +
				"    \"ssn\":\"123-12-345\",\n" +
				"    \"city\":\"Chicago\",\n" +
				"    \"Country\":\"U.S.\",\n" +
				"    \"email\":\"bage@qq.com\"\n" +
				" }";
		LOG.info(json);

	}
}
