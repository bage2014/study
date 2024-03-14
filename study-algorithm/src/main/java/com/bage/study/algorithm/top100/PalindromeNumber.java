package com.bage.study.algorithm.top100;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/palindrome-number/
 */
public class PalindromeNumber {

    public boolean isPalindrome(int x) {
        if(x < 0){
            return false;
        }

        List<Integer> list = new ArrayList<>();
        while (x > 0){
            list.add(x % 10);
            x = x / 10;
        }

        if(list.size() <= 1){
            return true;
        }
        for (int i = 0; i < list.size() /2; i++) {
            int j = list.size() - 1 - i;
            if(list.get(i) != list.get(j)){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(new PalindromeNumber().isPalindrome(123));
        System.out.println(new PalindromeNumber().isPalindrome(121));
        System.out.println(new PalindromeNumber().isPalindrome(-121));
        System.out.println(new PalindromeNumber().isPalindrome(0));
    }

}
