package com.bage.study.algorithm.niuke;
import java.util.ArrayList;

/**
 * 输入一个字符串,按字典序打印出该字符串中字符的所有排列。例如输入字符串abc,则打印出由字符a,b,c所能排列出来的所有字符串abc,acb,bac,bca,cab和cba。
 * 
 * https://www.nowcoder.com/practice/fe6b651b66ae47d7acce78ffdd9a96c7?tpId=13&tqId=11180&tPage=2&rp=2&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking
 * 
 * @author bage
 *
 */
public class ZidianxuSort2 {
    public ArrayList<String> Permutation(String str) {
    	ArrayList<String> list = new ArrayList<String>();
    	if(str == null || str.equals("")){
    		return new ArrayList<String>();
    	}
    	int a[] = new int[str.length()];
//    	for (int i = 0; i < a.length; i++) {
//			a[i] = i;
//		}
    	work(0,a,str,list);
    	return list;
    }

	private void work(int i, int[] a, String str, ArrayList<String> list) {
		if(i >= str.length()){
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < a.length; j++) {
				sb.append(str.charAt(a[j]));
			}
			list.add(sb.toString());
			return ;
		}
		
		for (int j = 0; j < a.length; j++) {
			if(isOk(j,i,a,str)){
				a[i] = j;
				work(i + 1,a,str,list);
			}
		}
		
	}
	
	private boolean isOk(int j, int i, int[] a, String str) {
		for (int k = 0; k < i; k++) {
			if(j == a[k] || (j < a[k] && str.charAt(j) == str.charAt(a[k]))){
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		
		ArrayList<String> list = new ZidianxuSort2().Permutation("aa");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
}