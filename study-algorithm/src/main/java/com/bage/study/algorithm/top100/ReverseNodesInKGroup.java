package com.bage.study.algorithm.top100;

import com.bage.study.algorithm.common.ListNode;

/**
 * https://leetcode.cn/problems/reverse-nodes-in-k-group/description/
 */
public class ReverseNodesInKGroup {

    public static void main(String[] args) {

    }
    public ListNode reverseKGroup(ListNode head, int k) {
        int count = 0;
        ListNode current = head;
        while (current != null){
            count = count + 1;
            if(count == k){
                // 重置 + 跳过
                count = 0;
                continue;
            }
            current = current.next;
        }
        return head;
    }

}
