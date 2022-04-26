package com.bage.study.log.logback;

// Add the following to the imports section of your java code:
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LogAppTest
 */
public class MaskLogTest {

	private static final Logger LOG = LoggerFactory.getLogger(MaskLogTest.class);


	@org.junit.Test
	public void testMasking() {
		// Throw some logging statements in your code somewhere where you know they'll be fired right away when you run your app. For example:
		String json = "{\n" +
				"    \"user_id\":\"87656\",\n" +
				"    \"ssn\":\"123-12-3453\",\n" +
				"    \"city\":\"Chicago\",\n" +
				"    \"Country\":\"U.S.\",\n" +
				"    \"email\":\"bage@qq.com\"\n" +
				" }";
		LOG.info(json);
	}

}
