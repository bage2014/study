package com.bage.study.algorithm.leetcode.longestsubstringwithoutrepeatingcharacters;

/**
 * https://leetcode.cn/problems/longest-substring-without-repeating-characters/
 */
class Solution {
    public int lengthOfLongestSubstring(String str) {
        if (str.length() <= 1) {
            return str.length();
        }
        String subStr = str.substring(0, str.length() - 1);
        if (subStr.contains(str.charAt(str.length() - 1) + "")) {
            return lengthOfLongestSubstring(subStr);
        }
        return lengthOfLongestSubstring(subStr) + 1;
    }

    public int lengthOfLongestSubstringBasic(String s) {
        return lengthOfLongestSubstringDetail(s).length();
    }

    public String lengthOfLongestSubstringDetail(String str) {
        if (str.length() == 0) {
            return "";
        }
        String temp = str.charAt(0) + "";
        for (int i = 0; i < str.length(); i++) {
            for (int j = i + 1; j < str.length(); j++) {
                String sub = str.substring(i, j);
                if (sub.contains(str.charAt(j) + "")) {
                    break;
                }
                if (sub.length() >= temp.length()) {
                    temp = sub + str.charAt(j);
                }
            }
        }
        System.out.println(str + ";;;; max sub: " + temp);
        return temp;
    }

    public static void main(String[] args) {
        System.out.println(new Solution().lengthOfLongestSubstring("abcabcbb"));
        System.out.println(new Solution().lengthOfLongestSubstring("bbbb"));
        System.out.println(new Solution().lengthOfLongestSubstring("pwwkew"));
    }
}