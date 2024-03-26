package com.bage.study.algorithm.top100;

import com.bage.study.algorithm.DataUtils;
import com.bage.study.algorithm.common.ListNode;

/**
 * 移除点名第N个
 * https://leetcode.cn/problems/remove-nth-node-from-end-of-list/description/
 */
public class RemoveNthNodeFromHeadOfList {
    public static void main(String[] args) {
        DataUtils.print(new RemoveNthNodeFromHeadOfList()
                .removeNthFromEnd(DataUtils.init(1, 2, 3, 4, 5, 6, 7), 3));

        DataUtils.print(new RemoveNthNodeFromHeadOfList()
                .removeNthFromEnd(DataUtils.init(1, 2, 3, 4, 5, 6, 7), 10));

        DataUtils.print(new RemoveNthNodeFromHeadOfList()
                .removeNthFromEnd(DataUtils.init(1, 2, 3, 4, 5, 6, 7), 0));

        DataUtils.print(new RemoveNthNodeFromHeadOfList()
                .removeNthFromEnd(DataUtils.init(1, 2, 3, 4, 5, 6, 7), 1));
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        if(head == null){
            return null;
        }
        ListNode current = head;

        if(n <= 1){
            if(n == 0){
                return head;
            }
            current = current.next;
            return current;
        }
        if(n >= 2){
            for (int i = 0; i < n - 2; i++) {
                if (current == null) {
                    break;
                }

                current = current.next; // 后移动
            }
        }

        if (current == null) {
            return head;
        }


        if (current.next != null) {
            // 删除current 元素
            current.next = current.next.next;
        }
        return head;
    }

}
