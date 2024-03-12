package com.bage.study.algorithm.top100;

import com.bage.study.algorithm.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/zigzag-conversion/description/
 */
public class ZigzagConversion {

    public static void main(String[] args) {
        new ZigzagConversion().convert("PAYPALISHIRING", 3);
        new ZigzagConversion().convert("PAYPALISHIRING", 4);

    }

    public String convert(String str, int numRows) {
        // 初始化
        List<List<String>> listList = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            List<String> list = new ArrayList<>();
            for (int j = 0; j < str.length(); j++) {
                list.add("");
            }
            listList.add(list);
        }

        //
        int length = str.length();
        int down = 1, up = 0;

        int flag = down;

        int row = -1;
        int col = 0;

        for (int i = 0; i < length; i++) {
            String charAt = "" + str.charAt(i);
            if(flag == down){
                row++;
                listList.get(row).set(col,charAt);
                if(row == numRows-1){
                    flag = up;
                }
            } else if(flag == up){
                row --;
                col ++;
                listList.get(row).set(col,charAt);
                if(row == 0){
                    flag = down;
                }
            }
        }

        String string = DataUtils.toString(listList);
        System.out.println(string);
        return "";
    }


}
