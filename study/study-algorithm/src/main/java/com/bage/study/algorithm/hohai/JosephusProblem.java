package com.bage.study.algorithm.hohai;

/**
 * 
 * 
 * <pre>
 约瑟夫问题：
 N个人围成一圈，从第一个开始报数，第M个将被杀掉，最后剩下一个，其余人都将被杀掉。
 例如N=6，M=5，被杀掉的顺序是：5，4，6，2，3，1。
分析：
（1）由于对于每个人只有死和活两种状态，因此可以用布朗型数组标记每个人的状态，可用true表示死，false表示活。
（2）开始时每个人都是活的，所以数组初值全部赋为false。
（3）模拟杀人过程，直到所有人都被杀死为止。
 参考链接：
 https://baike.baidu.com/item/%E7%BA%A6%E7%91%9F%E5%A4%AB%E9%97%AE%E9%A2%98/3857719?fr=aladdin

 * </pre>
 * @author bage
 *
 */
public class JosephusProblem {

	public static void main(String[] args) {
		
		int n = 10; // 人数
		int m = 5; // 报数
		
		int[] a = work(n,m,true);
		System.out.println("淘汰顺序：");
		print(a);
	}

	/**
	 * 
	 * @param n
	 * @param m
	 * @param isDebug 
	 * @return
	 */
	private static int[] work(int n, int m, boolean isDebug) {
		
		int[] res = new int[n];
		
		int[] a = new int[n]; // 0  都是有效的
		if(isDebug){
			for (int i = 0; i < a.length; i++) {
				a[i] = 1 + i;
			}
			print(a);
		}
		
		//  模拟退出
		int index = 0;
		int count = 0;
		int i = 0;
		while (i < n) { // 淘汰掉所有人
			while (i < n && count < m) { // 每次数到 m
				if(a[index] != -1){ // 人还在，没有被淘汰
					count ++;
				}
				if(count == m){
					a[index] = -1; // 淘汰
					res[i] = index + 1; // 记录结果
					if(isDebug){
						print(a);
					}
					count = 0;
					i ++ ;
				}
				index ++; // 下一个
				index = index % n; // 防止下标越界
			}
		}
		return res;
	}

	private static void print(int[] a) {
		for (int i = 0; i < a.length; i++) {
			if(a[i] != -1){
				System.out.print(a[i]);
			} else {
				System.out.print("淘汰");
			}
			System.out.print("\t");
		}
		System.out.println();
	}
	
}
