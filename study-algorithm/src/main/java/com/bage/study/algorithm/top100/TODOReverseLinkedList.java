package com.bage.study.algorithm.top100;

import com.bage.study.algorithm.DataUtils;
import com.bage.study.algorithm.common.ListNode;

/**
 * https://leetcode.cn/problems/reverse-linked-list/description/
 */
public class TODOReverseLinkedList {

    public static void main(String[] args) {
        ListNode node = DataUtils.init(1, 2, 3, 4, 5);
        new TODOReverseLinkedList().reverseListR(node);
        DataUtils.print(node);
    }

    public void reverseList(ListNode head) {
        if (head == null) {
            return;
        }

        ListNode result = head;
        if (head.next == null) {
            return;
        }

        while (head.next.next != null) {

        }
        ListNode next2 = head.next.next;

        // 交换 head 和 head.next
        ListNode temp = head;
        head = head.next;
        head.next = temp;
        temp.next = next2; // 原来的链

        reverseList(temp.next);
    }


    public ListNode reverseListR(ListNode head) {
        if (head == null) {
            return head;
        }
        if (head.next == null) {
            return head;
        }

        ListNode temp = head.next.next;

        // 交换 head 和 head.next
        head.next.next = head;
        head.next = temp; // 原来的链
        return reverseListR(head.next);
    }
}
