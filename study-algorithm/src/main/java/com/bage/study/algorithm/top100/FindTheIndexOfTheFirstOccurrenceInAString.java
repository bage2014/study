package com.bage.study.algorithm.top100;

/**
 * https://leetcode.cn/problems/find-the-index-of-the-first-occurrence-in-a-string/
 */
public class FindTheIndexOfTheFirstOccurrenceInAString {

    public static void main(String[] args) {

        System.out.println(new FindTheIndexOfTheFirstOccurrenceInAString().strStr("sadbutsad","sad"));
        System.out.println(new FindTheIndexOfTheFirstOccurrenceInAString().strStr("sadbutsad","tsad"));
        System.out.println(new FindTheIndexOfTheFirstOccurrenceInAString().strStr("sadbutsad",""));
        System.out.println(new FindTheIndexOfTheFirstOccurrenceInAString().strStr("",""));
        System.out.println(new FindTheIndexOfTheFirstOccurrenceInAString().strStr("","aaa"));
        System.out.println(new FindTheIndexOfTheFirstOccurrenceInAString().strStr("abc","aaa"));
        System.out.println(new FindTheIndexOfTheFirstOccurrenceInAString().strStr("abc","dd"));
    }
    public int strStr(String haystack, String needle) {
        if(haystack == null || haystack.isEmpty()
                || needle == null || needle.isEmpty()){
            return -1;
        }

        for (int i = 0; i < haystack.length(); i++) {
            boolean isMatch = true;
            for (int j = 0; j < needle.length(); j++) {
                if(i + j >= haystack.length()){
                    isMatch = false;
                    break;
                }
                if(haystack.charAt(i+j) != needle.charAt(j)){
                    isMatch = false;
                    break;
                }
            }
            if(isMatch){
                return i;
            }
        }
        return -1;
    }


}
