package com.bage.study.algorithm.top100;

/**
 * https://leetcode.cn/problems/longest-common-prefix/description/
 */
public class LongestCommonPrefix {

    public static void main(String[] args) {

        System.out.println(new LongestCommonPrefix().longestCommonPrefix(new String[]{"flower", "flow", "flight"}));
    }

    public String longestCommonPrefix(String[] strs) {
        StringBuilder commonPrefix = new StringBuilder("");
        if (strs.length == 0) {
            return commonPrefix.toString();
        }
        String firstStr = strs[0];
        if (strs.length == 1) {
            // 返回本身
            return firstStr;
        }

        for (int i = 0; i < firstStr.length(); i++) {
            char charAt = firstStr.charAt(i);
            for (int j = 0; j < strs.length; j++) {
                if (i >= strs[j].length() || strs[j].charAt(i) != charAt) {
                    // 长度超了，或者不匹配
                    // 直接结束
                    return commonPrefix.toString();
                }
            }

            // 都相同
            commonPrefix.append(charAt);
        }

        return commonPrefix.toString();
    }

}
