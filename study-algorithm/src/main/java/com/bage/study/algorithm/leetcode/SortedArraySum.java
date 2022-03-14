package com.bage.study.algorithm.leetcode;

import java.util.Objects;

/**
 * https://leetcode-cn.com/problems/kLl5u1/
 * SortedArraySum
 */
public class SortedArraySum {
    public ListNode reverseList(ListNode head) {
        if(Objects.isNull(head) || Objects.isNull(head.next)){
            return head;
        }
        ListNode listNode = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return listNode;
    }
}
