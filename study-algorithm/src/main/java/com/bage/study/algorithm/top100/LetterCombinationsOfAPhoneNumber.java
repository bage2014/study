package com.bage.study.algorithm.top100;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/letter-combinations-of-a-phone-number/description/
 */
public class LetterCombinationsOfAPhoneNumber {

    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if(digits == null || digits.length() == 0){
            return result;
        }
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < digits.length(); i++) {
            List<String> item = new ArrayList<>();
            item.add(digits.charAt(i) + "");
            list.add(item);
        }

        int indexFirst = 0;
        int indexLast = 0;
        while (true) {
            if(indexFirst >= list.get(0).size()
                && indexLast >= list.get(list.size() - 1).size()){
                break;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i).get(indexFirst));
            }
            result.add(sb.toString());

            
        }
        return result;
    }


}
