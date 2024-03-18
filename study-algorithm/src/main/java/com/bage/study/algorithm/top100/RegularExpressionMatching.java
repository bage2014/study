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
            char charAtPatternNext = charAtNext(indexPattern,pattern);

            if(charAtPattern == '.'){
                if(charAtPatternNext == '*'){
                    // 后续不用在看，肯定匹配
                    return true;
                }
                // else 相当于匹配
                indexStr ++;
                indexPattern ++;
            } else if(charAtPattern == '*'){
                indexStr ++;
                indexPattern ++;
            } else {
                // 其他情况，比如普通字符，， ABCD
                indexStr ++;
                if(charAtPatternNext == '*'){
                    indexPattern ++;
                }
            }

            if(indexStr > str.length() || indexPattern < pattern.length()){
                break;
            }
        }

        return false;
    }

    private char charAtNext(int indexPattern, String pattern) {
        if(indexPattern >= pattern.length()){
            return ' ';
        }
        return pattern.charAt(indexPattern + 1);
    }

}
