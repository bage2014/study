package com.bage.study.springboot.match;

import org.springframework.util.AntPathMatcher;

public class AntPathMatcherTest {

    public static void main(String[] args) {

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String pattern = "/bage1/**";
        String path1 = "/bage1";
        String path2 = "/bage1/bage2";
        boolean match1 = antPathMatcher.match(pattern, path1);
        boolean match2 = antPathMatcher.match(pattern, path2);
        System.out.println("match=" + match1 + "; pattern=" + pattern + "; path=" + path1);
        System.out.println("match=" + match2 + "; pattern=" + pattern + "; path=" + path2);


    }

}
