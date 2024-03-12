package com.bage.study.algorithm.top100;

/**
 * https://leetcode.cn/problems/zigzag-conversion/description/
 */
public class ZigzagConversion {


    public String convert(String str, int numRows) {
        char[][] chars = new char[numRows][];
        int length = str.length();

        int down = 1, up = 0;

        int flag = down;
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if(flag == down){

            }

        }

        return "";
    }


}
