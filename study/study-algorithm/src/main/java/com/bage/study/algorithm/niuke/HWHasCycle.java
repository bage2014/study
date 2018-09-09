package com.bage.study.algorithm.niuke;

import java.util.HashMap;
import java.util.Map;

/**
 * 判断链表是否有环
 * https://www.nowcoder.com/practice/253d2c59ec3e4bc68da16833f79a38e4?tpId=13&&tqId=11208&rp=1&ru=/activity/oj&qru=/ta/coding-interviews/question-ranking
 * @author bage
 *
 */
public class HWHasCycle {

	public static void main(String[] args) {
		
		
	}
	
	public ListNode EntryNodeOfLoop(ListNode pHead){
		
		Map<String,ListNode> map = new HashMap<String,ListNode>();
		ListNode isExistItem = null;
		String key = null;
		while(pHead != null){
			key = String.valueOf(pHead.val);
			isExistItem = map.get(key);
			if(isExistItem == null){
				map.put(key, pHead);
			} else {
				break;
			}
			pHead = pHead.next;
		}
		
        return pHead;
    }
}
class ListNode {
    int val;
    ListNode next = null;

    ListNode(int val) {
        this.val = val;
    }
}