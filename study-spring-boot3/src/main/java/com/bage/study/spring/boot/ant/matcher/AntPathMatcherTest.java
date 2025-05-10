package com.bage.study.spring.boot.ant.matcher;

import org.springframework.util.AntPathMatcher;

import java.util.Map;

/**
 * https://juejin.cn/post/7497173460423753740
 */
public class AntPathMatcherTest {

    public static void main(String[] args) {
        AntPathMatcher matcher = new AntPathMatcher();
        boolean match1 = matcher.match("/users/*", "/users/123");  // true
        boolean match2 = matcher.match("/users/**", "/users/123/orders");  // true
        boolean match3 = matcher.match("/user?", "/user1");  // true

        // 提取路径变量
        Map<String, String> vars = matcher.extractUriTemplateVariables(
                "/users/{id}", "/users/42");  // {id=42}

        System.out.println("match1 = " + match1);
        System.out.println("match2 = " + match2);
        System.out.println("match3 = " + match3);

        System.out.println(vars);
    }

}
