package com.bage.study.algorithm.leetcode.wordbreak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * https://leetcode.cn/problems/word-break/
 */
class Solution {
    public boolean wordBreak(String str, List<String> wordDict) {
        if (str.isEmpty()) {
            return true;
        }
        for (String item : wordDict) {
            if (str.startsWith(item)) {
                String substring = str.replaceFirst(item, "");
                if (wordBreak(substring, wordDict)) {
                    return true;
                }
            }
            if (str.endsWith(item)) {
                String substring = str.substring(0, str.length() - item.length());
                if (wordBreak(substring, wordDict)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println((new Solution().wordBreak("ccbb", Arrays.asList("bc", "cb"))));
        System.out.println((new Solution().wordBreak("leetcode", Arrays.asList("leet", "code"))));
        System.out.println((new Solution().wordBreak("cars", Arrays.asList("car", "ca", "rs"))));
    }
}