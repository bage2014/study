package com.bage.study.algorithm;

import com.bage.study.algorithm.common.ListNode;

import java.util.*;

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
            a[i] = i + 1;
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
            System.out.println("->");
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

    public static boolean print(List<Integer> list) {
        if (list == null) {
            System.out.println("->");
            return false;
        }
        for (Integer temp : list) {
            System.out.print(temp);
            System.out.print("->");
        }
        System.out.println();
        return true;
    }

    public static boolean isContains(List<List<Integer>> result, List<Integer> temp) {
        if (result == null || result.isEmpty()) {
            return false;
        }
        if (temp == null || temp.isEmpty()) {
            return false;
        }
        for (List<Integer> integers : result) {
            if (isMatch(integers, temp)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isMatch(List<Integer> list1, List<Integer> list2) {
        if (list1 == null || list1.isEmpty()) {
            return false;
        }
        if (list2 == null || list2.isEmpty()) {
            return false;
        }
        if (list2.size() != list1.size()) {
            return false;
        }

        // init map
        Map<Integer, Integer> maps1 = new HashMap<>();
        for (Integer integer : list1) {
            maps1.put(integer, maps1.getOrDefault(integer, 0));
        }

        Map<Integer, Integer> maps2 = new HashMap<>();
        for (Integer integer : list2) {
            maps2.put(integer, maps2.getOrDefault(integer, 0));
        }

        // match each entry
        Iterator<Map.Entry<Integer, Integer>> iterator = maps1.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> next = iterator.next();
            Integer orDefault = maps2.getOrDefault(next.getKey(), 0);
            if (!Objects.equals(next.getValue(), orDefault)) {
                return false;
            }
        }
        return true;
    }
}
