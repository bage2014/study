package com.bage.study.algorithm.top100;

import com.bage.study.algorithm.common.ListNode;
import com.bage.study.algorithm.common.ListNodeUtils;

/**
 * https://leetcode.cn/problems/add-two-numbers/
 */
public class AddTwoNumbers {

    public static void main(String[] args) {
        ListNode node1 = null;
        ListNode node2 = null;
        ListNode listNode = null;
//      node1 = ListNodeUtils.init(2,4,3);
//      node2 = ListNodeUtils.init(5,6,4);
//      listNode = new Solution().addTwoNumbers(node1, node2);
//       ListNodeUtils.print(listNode);
//
//       node1 = ListNodeUtils.init(2,4,3);
//       node2 = ListNodeUtils.init(5,6,7);
//       listNode = new Solution().addTwoNumbers(node1, node2);
//       ListNodeUtils.print(listNode);

       node1 = ListNodeUtils.init(2,4,3);
       node2 = ListNodeUtils.init(5,6);
       listNode = new Solution().addTwoNumbers(node1, node2);
       ListNodeUtils.print(listNode);
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
        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            ListNode node1 = l1;
            ListNode node2 = l2;
            ListNode result = null; // 结果
            ListNode last = null; // 结果的最后一位
            int flag = 0; // 进位

            while (true){
                int current = node1.val + node2.val + flag;

                flag = current >= 10 ? 1 : 0;
                current = current % 10;

                ListNode newOne = new ListNode(current);
                if(result == null){ // 初始化
                    result = newOne;
                    last = newOne;
                } else {
                    last.next = newOne;
                    last = last.next; // 往后移动
                }

                node1 = node1.next;
                node2 = node2.next;

                if(node1 == null && node2 == null){
                    break;
                }

                if(node1 == null && node2 != null){
                    node1 = new ListNode(0);
                }
                if(node2 == null && node1 != null){
                    node2 = new ListNode(0);
                }
            }


            if(flag == 1){
                ListNode newOne = new ListNode(flag);
                last.next = newOne;
                last = last.next; // 往后移动
            }
            return result;
        }
    }


}
