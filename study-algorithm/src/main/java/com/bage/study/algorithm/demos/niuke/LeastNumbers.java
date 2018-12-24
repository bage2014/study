package com.bage.study.algorithm.demos.niuke;
import java.util.ArrayList;

/**
 * 输入n个整数，找出其中最小的K个数。例如输入4,5,1,6,2,7,3,8这8个数字，则最小的4个数字是1,2,3,4,
 * 
 * https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf?tpId=13&tqId=11182&tPage=2&rp=2&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking
 * 
 * @author bage
 *
 */
public class LeastNumbers {
	public ArrayList<Integer> GetLeastNumbers_Solution(int[] input, int k) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		if(input.length < k || k <= 0){
			return res;
		}
		for (int i = 0; i < k; i++) {
			res.add(Integer.MAX_VALUE);
		}
		for (int i = 0; i < input.length; i++) {
			if(input[i] < res.get(res.size() - 1)){ // 当前值小于最小数组的最后一个值
				// 可以进行插入
				insert(input[i],res);
			}
		}
		return res;
	}


	private void insert(int n, ArrayList<Integer> res) {
		// 放入最后
		res.set(res.size() - 1, n);
		// 往前移动
		int temp = 0;
		for (int i = res.size() - 1; i > 0; i--) {
			if(res.get(i) < res.get(i - 1)){
				// 交换
				temp = res.get(i);
				res.set(i, res.get(i - 1));
				res.set(i - 1,temp);
			}
		}
	}


	public static void main(String[] args) {

		ArrayList<Integer> i = new LeastNumbers().GetLeastNumbers_Solution(new int[] { 4,5,1,6,2,7,3,8 }, 4);
		System.out.println(i);
	}
}