package com.bage.study.algorithm.top100;

import com.bage.study.algorithm.DataUtils;
import com.bage.study.algorithm.common.ListNode;

/**
 * https://leetcode.cn/problems/remove-nth-node-from-end-of-list/description/
 */
public class TODORemoveNthNodeFromEndOfList {
    public static void main(String[] args) {
        DataUtils.print(new TODORemoveNthNodeFromEndOfList()
                .removeNthFromEnd(DataUtils.init(1,2), 2));

        DataUtils.print(new TODORemoveNthNodeFromEndOfList()
                .removeNthFromEnd(DataUtils.init(new int[]{1}), 1));

        DataUtils.print(new TODORemoveNthNodeFromEndOfList()
                .removeNthFromEnd(DataUtils.init(1, 2, 3, 4, 5, 6, 7), 3));

        DataUtils.print(new TODORemoveNthNodeFromEndOfList()
                .removeNthFromEnd(DataUtils.init(1, 2, 3, 4, 5, 6, 7), 10));

        DataUtils.print(new TODORemoveNthNodeFromEndOfList()
                .removeNthFromEnd(DataUtils.init(1, 2, 3, 4, 5, 6, 7), 0));

        DataUtils.print(new TODORemoveNthNodeFromEndOfList()
                .removeNthFromEnd(DataUtils.init(1, 2, 3, 4, 5, 6, 7), 1));
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null) {
            return null;
        }
        ListNode fast = head;
        ListNode low = head;

        if(n <= 1 && fast.next == null){
            return null;
        }

        int index = 0;
        while (fast.next != null){
            if(index >= n){ // 相差 N 个 ，
                low = low.next;
            }
            fast = fast.next; // 后移动
            index ++;
        }

        if (index >= n) { // 存在
            if(low.next != null){
                low.next = low.next.next;
            }
        } else {
            // 不存在，，

        }
        return head;
    }

}
