package com.bage.study.algorithm.top100;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/valid-parentheses/description/
 */
public class ValidParentheses {

    public static void main(String[] args) {
        System.out.println(new ValidParentheses().isValid("()"));
        System.out.println(new ValidParentheses().isValid("()[]"));
        System.out.println(new ValidParentheses().isValid("()[]{}"));
        System.out.println(new ValidParentheses().isValid("()[]{"));
        System.out.println(new ValidParentheses().isValid(""));
        System.out.println(new ValidParentheses().isValid("{{}}"));
        System.out.println(new ValidParentheses().isValid("(]"));
    }

    public boolean isValid(String str) {
        List<Character> list = new ArrayList<>();

        if (str == null || str.isEmpty()) {
            return false;
        }

        char[] chars = str.toCharArray();
        for (char charAt : chars) {
            if (charAt == '('
                    || charAt == '['
                    || charAt == '{') {
                list.add(charAt);
            } else if (charAt == ')'
                    || charAt == ']'
                    || charAt == '}') {

                if(list.isEmpty()){
                    // 无法移除，理解为 不符合
                    return false;
                }
                // else
                int last = list.size() - 1;
                if(!isMatch(list.get(last),charAt)){
                    // 无法移除，理解为 不符合
                    return false;
                }
                // else
                list.remove(last);
            }
        }
        return list.isEmpty();
    }

    private boolean isMatch(Character left, Character right) {
        if(left == '['){
            return right == ']';
        }
        if(left == '{'){
            return right == '}';
        }
        if(left == '('){
            return right == ')';
        }
        return false;
    }

}
