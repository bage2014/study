package com.bage.study.algorithm.string;

import java.util.*;

public class Solution {

    public static void main(String[] args){
        Solution solution = new Solution();
//        System.out.println(solution.isIsomorphic("ab", "aa"));

        System.out.println(solution.firstUniqChar("loveleetcode"));
    }


    /**
     * 给定两个字符串 s 和 t，判断它们是否是同构的。
     *
     * 如果 s 中的字符可以被替换得到 t ，那么这两个字符串是同构的。
     *
     * 所有出现的字符都必须用另一个字符替换，同时保留字符的顺序。两个字符不能映射到同一个字符上，但字符可以映射自己本身。
     *
     * 输入: s = "egg", t = "add"
     * 输出: true
     *
     * @param s
     * @param t
     * @return
     */
    public boolean isIsomorphic(String s, String t) {

        //字典法解决，但是要注意的一点是，可能存在多个key映射到同一个value的情况，因此要判断value是否唯一

        if(s.length() == t.length()){

            Map<Character, Character> map = new HashMap<>();

            Set<Character> values = new HashSet<>();

            for(int i = 0; i < s.length(); ++i){

                char c1 = s.charAt(i);

                char c2 = t.charAt(i);

                Character c = map.get(c1);
                if(c == null){
                    if(values.add(c2)){
                        map.put(c1, c2);
                    }else{
                        return false;
                    }
                }else{
                    if(c != c2){
                        return false;
                    }
                }
            }

            return true;
        }

        return false;

    }

    /**
     * 给定一个字符串，找到它的第一个不重复的字符，并返回它的索引。如果不存在，则返回 -1。
     * s = "leetcode"
     * 返回 0
     *
     * s = "loveleetcode"
     * 返回 2
     *
     * 提示：你可以假定该字符串只包含小写字母。
     *
     * @param s
     * @return
     */
    public int firstUniqChar(String s) {

        //方法一：笨方法，一个用来存储出现次数
        Map<Character, Integer> map = new HashMap<>();
        for(int i=0; i <s.length(); ++i){
            char c= s.charAt(i);
            Integer n = map.get(c);
            if(n == null){
                map.put(c, 1);
            }else{
                map.put(c,++n);
            }
        }

        for(int i=0; i<s.length(); ++i){
            if(map.get(s.charAt(i)) == 1){
                return i;
            }
        }

        return -1;

        //方法二，因为只有26个小写字母，所以可以用两个数组来当做字典，一个用来记录出现的位置，另外一个记录出现的次数

//        int [] n1 = new int[26];
//
//        int[] n2 = new int[26];
//
//
//        for(int i=0; i <s.length(); ++i){
//            char c= s.charAt(i);
//            int index = c - 'a';
//
//            n1[index] = i;
//            n2[index]++;
//        }
//
//        int min = -1;
//
//        for(int i=0; i<26; ++i){
//            if(n2[i] == 1){
//                if(min == -1){
//                    min = n1[i];
//                }else{
//                    if(n1[i]<min){
//                        min = n1[i];
//                    }
//                }
//            }
//        }
//
//
//        return min;
    }


    /**
     * 字符串压缩。利用字符重复出现的次数，编写一种方法，实现基本的字符串压缩功能。
     * 比如，字符串aabcccccaaa会变为a2b1c5a3。若“压缩”后的字符串没有变短，则返回原先的字符串。
     * 你可以假设字符串中只包含大小写英文字母（a至z）。
     * @param S
     * @return
     */
    public String compressString(String S) {

        //非常简单，一目了然

        int length = S.length();

        if(length>1){
            StringBuilder builder = new StringBuilder();

            char c = S.charAt(0);
            builder.append(c);
            int n = 1;
            for(int i=1; i<length; ++i){
                char c1 = S.charAt(i);
                if(c == c1){
                    n++;
                }else{
                    builder.append(n);
                    builder.append(c1);
                    n=1;
                    c = c1;
                }
            }
            builder.append(n);

            String s1 = builder.toString();
            if(s1.length()>=S.length()){
                return S;
            }else{
                return s1;
            }

        }

        return S;
    }

    /**
     * 给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写。
     *
     * 说明：本题中，我们将空字符串定义为有效的回文串。
     * @param s
     * @return
     */
    public boolean isPalindrome(String s) {

        //非常基本的算法，从两头遍历即可

        int length = s.length();

        if(length>1){
            int l = 0, r = length-1;

            while (true){

                //左边找到第一个是数字或者字母的下标
                while (l<length){

                    char c = s.charAt(l);

                    if(isNum(c) || isAlphabet(c)){
                        break;
                    }
                    l++;
                }

                //超出限制没找到，直接返回true
                if(l>=length){
                    return true;
                }

                //右边找到第一个是数字或者字母的下标
                while (r>-1){
                    char c = s.charAt(r);

                    if(isNum(c) || isAlphabet(c)){
                        break;
                    }
                    r--;
                }

                if(l>=r){
                    //如果已经完成遍历，直接返回true
                    return true;
                }else{
                    //判断两值是否相等
                    char c1 = s.charAt(l);
                    char c2 = s.charAt(r);
                    if(c1 != c2){
                        if(isAlphabet(c1) && isAlphabet(c2)){
                            if(calNum(c1) != calNum(c2)){
                                return false;
                            }
                        }else{
                            return false;
                        }
                    }
                }

                //一次循环后别忘记把两个下标前移
                l++;
                r--;

            }
        }




        return true;

    }


    private boolean isNum(char c){
        return c>='0' && c<='9';
    }

    private boolean isAlphabet(char c){
        return (c>='a' && c<='z') || (c>='A' && c<='Z');
    }

    private int calNum(char c){
        if(c>='a' && c<='z'){
            return c-'a';
        }else{
            return c-'A';
        }
    }

}
