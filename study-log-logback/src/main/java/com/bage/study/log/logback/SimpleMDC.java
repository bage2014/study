package com.bage.study.log.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.core.ConsoleAppender;

/**
 * https://www.baeldung.com/mdc-in-log4j-2-logback
 */
public class SimpleMDC {
  static public void main(String[] args) throws Exception {

    // You can put values in the MDC at any time. Before anything else
    // we put the first name
    MDC.put("myTag", "myTag-foo");


    Logger logger = LoggerFactory.getLogger(SimpleMDC.class);
    // The most beautiful two words in the English language according
    // to Dorothy Parker:
    logger.info("Check enclosed.");
    logger.debug("The most beautiful two words in English.");
    MDC.put("myTag", "myTag-bar");

    logger.info("I am not a crook.");
    logger.info("Attributed to the former US president. 17 Nov 1973.");
  }


}