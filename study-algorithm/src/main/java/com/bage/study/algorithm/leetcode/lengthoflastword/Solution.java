package com.bage.study.algorithm.leetcode.lengthoflastword;

/**
 * https://leetcode.cn/problems/length-of-last-word/
 *
 */
class Solution {
    public int lengthOfLastWord(String str) {
        int start = str.length() - 1;
        while (start >=0 && str.charAt(start) == ' ') {
            start --;
        }
        if (start < 0) {
            return 0;
        }
        for (int i = start; i >= 0; i--) {
            if (str.charAt(i) == ' ') {
                return start - i;
            }
        }
        return start + 1;
    }

    public static void main(String[] args) {
        System.out.println(new Solution().lengthOfLastWord("hhh hfjgshjgg"));
        System.out.println(new Solution().lengthOfLastWord("hhh "));
        System.out.println(new Solution().lengthOfLastWord("hhh h"));
        System.out.println(new Solution().lengthOfLastWord("hhh"));
        System.out.println(new Solution().lengthOfLastWord("h"));
    }

}