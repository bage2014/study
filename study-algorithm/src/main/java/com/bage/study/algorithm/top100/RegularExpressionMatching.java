package com.bage.study.algorithm.top100;

/**
 * https://leetcode.cn/problems/regular-expression-matching/
 */
public class RegularExpressionMatching {

    public static void main(String[] args) {
        System.out.println(new RegularExpressionMatching().isMatch("aa","aa"));
        System.out.println(new RegularExpressionMatching().isMatch("aa","a"));
        System.out.println(new RegularExpressionMatching().isMatch("ab","a*"));
        System.out.println(new RegularExpressionMatching().isMatch("ab",".*"));
    }

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

            if(charAtPattern == '.' && charAtPatternNext == '*'){
                    // 后续不用在看，肯定匹配
                    return true;
            }else if(charAtPattern == '.' && charAtPatternNext != '*'){
                // else 相当于匹配
                indexStr ++;
                indexPattern ++;
            } else if(charAtPattern != '.' && charAtPatternNext == '*'){
                // 普通字符匹配
                if(charAtPattern == charAtStr){
                    indexStr ++;
//                    indexPattern ++; // 不添加
                } else {
                    return false; // 不匹配
                }
            } else if(charAtPattern != '.' && charAtPatternNext != '*'){
                // 普通字符匹配
                if(charAtPattern == charAtStr){
                    indexStr ++;
                    indexPattern ++;
                } else {
                    return false; // 不匹配
                }
            } else {
                // 其他情况，比如普通字符，， ABCD
                if(charAtPattern == charAtStr){
                    indexStr ++;
                    indexPattern ++;
                } else {
                    return false; // 不匹配
                }
            }

            if(indexStr > str.length() || indexPattern < pattern.length()){
                break;
            }
        }

        return true;
    }

    private char charAtNext(int indexPattern, String pattern) {
        if(indexPattern >= pattern.length()){
            return ' ';
        }
        return pattern.charAt(indexPattern + 1);
    }

}
