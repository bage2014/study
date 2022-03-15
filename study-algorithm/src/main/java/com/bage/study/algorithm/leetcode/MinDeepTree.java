package com.bage.study.algorithm.leetcode;

import java.util.Objects;


/**
 * https://leetcode-cn.com/problems/minimum-depth-of-binary-tree/
 * // todo ddd
 */
public class MinDeepTree {
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

class TreeNode {
    TreeNode next;
 }
