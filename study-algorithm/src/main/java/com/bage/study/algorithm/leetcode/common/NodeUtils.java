package com.bage.study.algorithm.leetcode.common;

public class NodeUtils {

    public static boolean print(ListNode node) {
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
    public static ListNode copy(ListNode node) {
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


    public static ListNode init(int... arr) {
        ListNode next = new ListNode(arr[arr.length - 1]);
        ListNode prevNode = next;
        for (int i = 1; i < arr.length; i++) {
            prevNode = new ListNode(arr[arr.length - i - 1], next);
            next = prevNode;
        }
        return prevNode;
    }
}
