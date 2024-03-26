package com.bage.study.algorithm.common;

import com.bage.study.algorithm.DataUtils;

public class ListNodeUtils {

    public static boolean print(ListNode node) {
        DataUtils.print(node);
        return true;
    }
    public static ListNode copy(ListNode node) {
        return DataUtils.copy(node);
    }


    public static ListNode init(int... arr) {
        return DataUtils.init(arr);
    }
}
