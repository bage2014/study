package com.bage.study.algorithm.leetcode.removenthnodefromendoflist;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 * int val;
 * ListNode next;
 * ListNode() {}
 * ListNode(int val) { this.val = val; }
 * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */

import com.bage.study.algorithm.common.ListNode;

/**
 * https://leetcode.cn/problems/remove-nth-node-from-end-of-list/
 * <p>
 * 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
 */
class Solution {
    public ListNode removeNthFromEnd(ListNode node, int n) {
        ListNode offsetNode = node;
        // 相差 N 个节点
        for (int i = 0; i < n; i++) {
            if (offsetNode == null) {
                return node; // 相当于不需要处理
            }
            offsetNode = offsetNode.next;
        }

        // 继续找，找到末尾
        ListNode head = node;
        while (offsetNode.next != null) {
            offsetNode = offsetNode.next;
            head = head.next;
        }
        // 删除当前 head 节点即可
        head.next = head.next.next;
        return node;
    }

    public static void main(String[] args) {
        ListNode node5 = new ListNode(5);
        ListNode node4 = new ListNode(4, node5);
        ListNode node3 = new ListNode(3, node4);
        ListNode node2 = new ListNode(2, node3);
        ListNode head = new ListNode(1, node2);
        print(head);
        System.out.println("-------- after remove ");
        print(new Solution().removeNthFromEnd(copy(head), 1));
        print(new Solution().removeNthFromEnd(copy(head), 3));
        print(new Solution().removeNthFromEnd(copy(head), 8));
        print(new Solution().removeNthFromEnd(copy(head), 5));
        print(new Solution().removeNthFromEnd(null, 8));
    }

    private static boolean print(ListNode node) {
        if (node == null) {
            System.out.print("->");
            return false;
        }
        ListNode head = node;
        while (head != null) {
            System.out.print(head.val);
            System.out.print("->");
            head = head.next;
        }
        System.out.println();
        return true;
    }
    private static ListNode copy(ListNode node) {
        if (node == null) {
            return null;
        }
        ListNode result = new ListNode(node.val);
        ListNode resultNext = result;
        ListNode head = node;

        while (head.next != null) {
            head = head.next;

            resultNext.next = new ListNode(head.val);
            resultNext = resultNext.next;
        }

        return result;
    }
}

