package com.bage.study.algorithm;

import com.bage.study.algorithm.common.ListNode;

import java.util.List;
import java.util.Random;

public class DataUtils {

    public static void main(String[] args) {
        int[] a = DataUtils.init(9);
        print(a);
    }

    public static void print(int[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " \t");
        }
        System.out.println();
    }

    public static int[] init(int n) {
        int a[] = new int[n];
        for (int i = 0; i < a.length; i++) {
            a[i] = i+ 1;
        }
        Random random = new Random();
        int randomI = 0;
        int temp = 0;
        for (int i = 0; i < a.length; i++) {
            randomI = random.nextInt(n - i);
            temp = a[randomI];
            a[randomI] = a[i];
            a[i] = temp;
        }
        return a;
    }

    public static String toStringFromStr(List<List<String>> listList) {
        StringBuilder sb = new StringBuilder();
        for (List<String> list : listList) {
            for (String str : list) {
                sb.append(str).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    public static String toStringFromInt(List<List<Integer>> listList) {
        StringBuilder sb = new StringBuilder();
        for (List<Integer> list : listList) {
            for (Integer str : list) {
                sb.append(str).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


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
