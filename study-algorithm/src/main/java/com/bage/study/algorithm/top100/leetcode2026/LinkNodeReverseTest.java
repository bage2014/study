package com.bage.study.algorithm.top100.leetcode2026;

import com.bage.study.algorithm.DataUtils;
import com.bage.study.algorithm.common.ListNode;
import com.bage.study.algorithm.top100.TODOReverseLinkedList;

/**
 * https://leetcode.cn/search/?q=%E7%BF%BB%E8%BD%AC%E9%93%BE%E8%A1%A8
 *
 * 翻转列表
 */
public class LinkNodeReverseTest {
    public static void main(String[] args) {
        com.bage.study.algorithm.common.ListNode node = DataUtils.init(1, 2, 3, 4, 5);
        ListNode result = new Solution().reverseList(node);
        DataUtils.print(result);
    }
    /**
     * Definition for singly-linked list.
     * public class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode() {}
     *     ListNode(int val) { this.val = val; }
     *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    static class Solution {
        public ListNode reverseList(ListNode head) {
            // 1->2->3->4->5
            if(head.next == null){
                return head;
            }

            ListNode current = head;
            ListNode next = current.next;
            ListNode prev = null;

            while(true){
                current.next = prev;
                prev = current;
                current = next;
                next = next.next;
                if(next == null){
                    break;
                }
            }

            current.next = prev;
            // 5->4->3->2->1
            return current;
        }
    }


}
