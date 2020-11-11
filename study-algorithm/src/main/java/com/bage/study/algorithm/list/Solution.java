package com.bage.study.algorithm.list;

public class Solution {
    public static void main(String[] args){
        Solution solution = new Solution();
        int[] nums = {6,6,6,5,6,7,6};
        ListNode head = solution.buildList(nums);

        ListNode result = solution.removeElements(head,6);

        solution.printList(result);


    }

    public ListNode buildList(int[] nums){
        ListNode head = null;
        ListNode current = null;
        for(int i=0; i<nums.length; ++i){
            if(i == 0){
                head = new ListNode(nums[i]);
                current = head;
            }else{
                current.next = new ListNode(nums[i]);
                current = current.next;
            }
        }
        return head;
    }

    public void printList(ListNode root){
        if(root == null){
            System.out.println("null");
        }
        while (root!=null){
            System.out.println(root.val);
            root = root.next;
        }
    }

    /**
     * 删除链表中等于给定值 val 的所有节点。
     * @param head
     * @param val
     * @return
     */
    public ListNode removeElements(ListNode head, int val) {

        //因为要返回链表的头，所以要先找到链表的头，这是本题的难点所在

        ListNode current = head;

        //这个循环是为了找到要返回链表的头
        while (current != null && current.val == val){
            current = current.next;
        }

        if(current == null){
            return null;
        }

        if(current != head){
            head = current;
        }

        while (current.next != null){
            if(current.next.val == val){
                ListNode temp = current.next.next;
                current.next.next = null;
                current.next = temp;
            }else{
                current = current.next;
            }
        }

        return head;
    }

}
