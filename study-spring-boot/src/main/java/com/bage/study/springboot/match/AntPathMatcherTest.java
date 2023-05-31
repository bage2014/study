package com.bage.study.springboot.match;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class AntPathMatcherTest {

    public static void main(String[] args) {
        System.out.println(match("/bage1/**","/bage1"));
        System.out.println(match("/bage1/**","/bage1/bage2"));
        System.out.println(match("/bage1/*","/bage1/bage3"));
        System.out.println(match("/bage1/*","/bage1"));
        System.out.println(match("/bage1/*","/bage1/bage3/2/32222"));
        System.out.println(match("/bage1/*/**","/bage1/bage3/2/32222"));
        System.out.println(match("/bage1/*/**","/bage1"));
        System.out.println(match("/bage1/*/**","/bage1/122"));

    }

    private static String match(String pattern, String path) {
        PathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match(pattern, path);
        String result = ("match=" + match + "; pattern=" + pattern + "; path=" + path);
        return result;
    }

}
