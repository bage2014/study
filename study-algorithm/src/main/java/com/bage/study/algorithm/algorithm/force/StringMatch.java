package com.bage.study.algorithm.algorithm.force;

/**
 * 字符串匹配
 */
public class StringMatch {

    public static void main(String[] args) {

        String a = "ancddfgddg";
        String b = "fgdg";

        int index = new StringMatch().match(a,b);
        System.out.println(index);
    }

    /**
     * 字符串暴力匹配
     * @param a
     * @param b
     * @return
     */
    private int match(String a, String b) {
        int j = 0;
        for (int i = 0; i < a.length(); i++) {
            for (j = 0; j < b.length(); j++) {
                if(a.charAt(i) != b.charAt(j)){ // 不匹配
                    break ;
                } else {
                    i ++; // 匹配，继续往下进行比较
                }
            }
            if(j == b.length()){ // 匹配成功
                return i - b.length();
            }
        }
        return -1;
    }

}
