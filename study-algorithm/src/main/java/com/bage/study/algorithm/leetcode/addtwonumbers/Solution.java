package com.bage.study.algorithm.leetcode.addtwonumbers;

import com.bage.study.algorithm.common.ListNode;
import com.bage.study.algorithm.common.ListNodeUtils;

/**
 * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
 * <p>
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 * <p>
 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * <p>
 * Definition for singly-linked list.
 * public class ListNode {
 * int val;
 * ListNode next;
 * ListNode() {}
 * ListNode(int val) { this.val = val; }
 * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head1 = l1;
        ListNode head2 = l2;
//        while(){
//
//        }
        return null;
    }

    public static void main(String[] args) {
        ListNode l1 = ListNodeUtils.init(5,4,3);
        ListNode l2 = ListNodeUtils.init(5,4,3);
        ListNode listNode = new Solution().addTwoNumbers(l1, l2);
        ListNodeUtils.print(listNode);
    }

}

