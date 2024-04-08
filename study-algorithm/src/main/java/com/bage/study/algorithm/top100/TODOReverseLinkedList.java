package com.bage.study.algorithm.top100;

import com.bage.study.algorithm.DataUtils;
import com.bage.study.algorithm.common.ListNode;

/**
 * https://leetcode.cn/problems/reverse-linked-list/description/
 */
public class TODOReverseLinkedList {

    public static void main(String[] args) {
        ListNode node = DataUtils.init(1,2,3,4,5);
        ListNode result = new TODOReverseLinkedList().reverseList(node);
        DataUtils.print(result);
    }
    public ListNode reverseList(ListNode head) {
        if(head == null){
            return null;
        }
        return null;
    }
}
