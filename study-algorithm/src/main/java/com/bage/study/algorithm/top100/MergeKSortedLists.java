package com.bage.study.algorithm.top100;

import com.bage.study.algorithm.DataUtils;
import com.bage.study.algorithm.common.ListNode;

/**
 * https://leetcode.cn/problems/merge-k-sorted-lists/description/
 */
public class MergeKSortedLists {

    public static void main(String[] args) {
        ListNode[] lists = new ListNode[]{
                DataUtils.init(1, 4, 5),
                DataUtils.init(1, 3, 4),
                DataUtils.init(2, 6),
        };
        DataUtils.print(new MergeKSortedLists().mergeKLists(lists));
    }

    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        if (lists.length == 1) {
            return lists[0];
        }

        ListNode resultCurrent = null;
        ListNode result = null;

        while (true) {
            ListNode minHead = null;
            int minHeadIndex = -1;
            for (int i = 0; i < lists.length; i++) {
                ListNode head = lists[i];
                if (head == null) {
                    continue;
                }
                if (minHead == null) {
                    minHead = head; // 初始化
                    minHeadIndex = i;
                }

                if (head.val < minHead.val) {
                    // 替换
                    minHead = head;
                    minHeadIndex = i;
                }
            }
            if (minHead == null) {
                break; // 结束
            }
            if (resultCurrent == null) {
                // 首次赋值，
                resultCurrent = new ListNode(minHead.val);
                result = resultCurrent;
            } else {
                // 添加都后继节点
                resultCurrent.next = new ListNode(minHead.val);
                resultCurrent = resultCurrent.next;
            }
            minHead = minHead.next;
            lists[minHeadIndex] = minHead;
        }

        return result;
    }
}
