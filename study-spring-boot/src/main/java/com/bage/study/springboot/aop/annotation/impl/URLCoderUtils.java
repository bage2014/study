package com.bage.study.springboot.aop.annotation.impl;

import java.io.UnsupportedEncodingException;

public class URLCoderUtils {
    private static final String enc= "UTF-8";

    public static String decode(String value) {
        try {
            return java.net.URLDecoder.decode(value,enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encode(String value) {
        try {
            return java.net.URLEncoder.encode(value,enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
