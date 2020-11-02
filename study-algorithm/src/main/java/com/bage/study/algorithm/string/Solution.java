package com.bage.study.algorithm.string;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Solution {

    public static void main(String[] args){
        Solution solution = new Solution();
        System.out.println(solution.isIsomorphic("ab", "aa"));
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
}
