package com.bage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *o
 */
public class DataSourceContextHolder {

    private static final Logger log = LoggerFactory.getLogger(DataSourceContextHolder.class);

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    /**
     * @param db
     */
    public static void set(String db) {
        log.info("set db = {}", db);
        contextHolder.set(db);
    }

    /**
     * @return
     */
    public static String get() {
        return contextHolder.get();
    }
}
