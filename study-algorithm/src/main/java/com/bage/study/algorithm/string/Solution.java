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
}
