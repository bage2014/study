package com.bage.study.algorithm.leetcode.longestpalindromicsubstring;

/**
 * https://leetcode.cn/problems/longest-palindromic-substring/
 */
class Solution {
    public String longestPalindrome22(String str) {
        if (str.isEmpty()) {
            return "";
        }
        if (str.length() == 1) {
            return str.charAt(0) + "";
        }
        String max = "";
        for (int i = 0; i < str.length(); i++) {
            for (int j = i + 1; j <= str.length(); j++) {
                String sub = str.substring(i, j);
                if (isMatch(sub)) {
                    if (sub.length() > max.length()) {
                        max = sub;
                    }
                }
            }
        }
        return max;
    }

    private boolean isMatch(String sub) {
        if (sub.isEmpty() || sub.length() == 1) {
            return true;
        }
        for (int i = 0; i < sub.length() / 2; i++) {
            if (sub.charAt(i) != sub.charAt(sub.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }


    public String longestPalindrome(String str) {
        if (str.isEmpty()) {
            return "";
        }
        if (str.length() == 1) {
            return str.charAt(0) + "";
        }

        String max = str.charAt(0) + "";
        for (int i = 0; i < str.length(); i++) {
            int middle = i;
            String temp = isMatch(str, middle, true);
            if (temp.length() > max.length()) {
                max = temp;
            }
            temp = isMatch(str, middle, false);
            if (temp.length() > max.length()) {
                max = temp;
            }
        }
        return max;
    }

    private String isMatch(String sub, int middle, boolean isVirtual) {
        int i = 1;
        String str = "";
        int left = middle - getIndex(isVirtual, i);
        int right = middle + i;
        while (left >= 0 && right < sub.length()) {
            if (sub.charAt(left) != sub.charAt(right)) {
                break;
            }
            // 肯定相同
            str = sub.substring(left, right + 1);
            i++;
            left = middle - getIndex(isVirtual, i);
            right = middle + i;
        }
        return str;
    }

    private int getIndex(boolean isVirtual, int i) {
        return isVirtual ? (i - 1) : i;
    }

    public static void main(String[] args) {
        System.out.println(new Solution().longestPalindrome("abccbaabcbb"));
        System.out.println(new Solution().longestPalindrome("bb"));
        System.out.println(new Solution().longestPalindrome("babad"));
        System.out.println(new Solution().longestPalindrome("cbbd"));
    }
}
