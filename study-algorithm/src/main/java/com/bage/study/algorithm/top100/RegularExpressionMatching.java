package com.bage.study.algorithm.top100;

/**
 * https://leetcode.cn/problems/regular-expression-matching/
 */
public class RegularExpressionMatching {

    public boolean isMatch(String str, String pattern) {
        if(pattern == null && str == null){
            return true;
        }
        if(pattern == str){
            return true;
        }

        if(str == null || pattern == null){
            return false;
        }

        int indexStr = 0;
        int indexPattern = 0;
        while (true){
            char charAtStr = str.charAt(indexStr);
            char charAtPattern = pattern.charAt(indexPattern);

            if(charAtPattern == '.'){
                indexStr ++;
                indexPattern ++;
            } else if(charAtPattern == '*'){
                indexStr ++;
                indexPattern ++;
            }

            if(indexStr > str.length() || indexPattern < pattern.length()){
                break;
            }
        }

        return false;
    }

}
