package com.bage.study.java.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 *
 ------
 著作权归@pdai所有
 原文链接：https://pdai.tech/md/interview/x-interview.html
 */
public class TestCase {
    public static void main(String[] args) {
        ServiceLoader<Search> s = ServiceLoader.load(Search.class);
        Iterator<Search> iterator = s.iterator();
        while (iterator.hasNext()) {
           Search search =  iterator.next();
           search.searchDoc("hello world");
        }


    }
}