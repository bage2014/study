package com.bage.study.java;

/**
 * Jvm 相关知识测试:
 * 参考链接：<br>
 * https://blog.csdn.net/wenhuayuzhihui/article/details/51981924<br>
 * https://blog.csdn.net/yanghw117/article/details/80889298<br>
 * 
 * @author bage
 *
 */
public class Jvm {

	private static int deep = 0;
	
	public static void main(String[] args) {
		
		soe(); // java.lang.StackOverflowError // 堆栈溢出SOE
	}

	private static void soe() {
		//       ---- 堆内存设置：程序可以到达的，可以操作的
//	       -Xms 初始堆内存 默认物理内存1/64,也是最小分配堆内存。当空余堆内存小于40%时，会增加到-Xms的最大限制 
//
//	       -Xmx 最大堆内存分配 默认物理内存1/4，当空余堆内存大于70%时，会减小到-Xms的最小限制。
//	                    一般设置 -Xms和Xms大小相等

//	       ---- 非堆内存设置
//	      -XX:PermSize 非堆内存的初始值，默认物理内存的1/64 ，也是最小非堆内存。
//	      -XX:MaxPermSize 非堆内存最大值，默认物理内存的1/4,


		// int n = 1000000; // java.lang.StackOverflowError
		// int n = 10; // 正常调用
		
		int n = 10000000;
		// 10462  eclipse 默认堆栈大小
		// System.out.println(Integer.toBinaryString(10462));
		try {
			work(n);

		} catch (Throwable e) {
			System.out.println(deep);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 2394 	-Xss256k
		// 904		-Xss128k
		// 4521090	-Xss128m
		// 18458 	默认
		//  -Xss128k：设置每个线程的堆栈大小。 JDK5.0以后每个线程堆栈大小为1M，以前每个线程堆栈大小为256K。
		// 根据应用的线程所需内存大小进行调整。在相同物理内存下，减小这个值能生成更 多的线程。
		// 但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。
	}

	private static int work(int n) {
		deep ++ ;
		if(n <= 0){
			return 0;
		}
		return n + work(n - 1);
		
	}
	
}
