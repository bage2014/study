package com.bage.study.algorithm.demos.xiaomi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main2 {

	public static void main(String[] args) {

		Scanner s = new Scanner(System.in);
		int n = s.nextInt();

		ArrayList<Set> in = new ArrayList<Set>();
		s.nextLine();
		for (int i = 0; i < n; i++) {
			readLine(s, in);
		}
		int max = -1;
		System.out.println(in.size());
		for (Set ss : in) {
			max = ss.size() > max ? ss.size() : max;
		}
		System.out.println(max);

	}

	private static void readLine(Scanner s, ArrayList<Set> in) {
		String num = s.nextLine();
		String[] ns = num.split(" ");
		Set<Integer> ts = new HashSet<Integer>();
		for (String n : ns) {
			if(n == "")continue;
			ts.add(Integer.valueOf(n));
		}
		if (in.size() != 0) {
			retain(in, ts);
		} else {
			in.add(ts);
		}

	}

	private static void retain(ArrayList<Set> in, Set<Integer> ts) {
		Set set = new HashSet();
		for (int i = 0; i < in.size(); i++) {
			HashSet<Integer> s = (HashSet) in.get(i);
			for(Integer tt : s){
				set.add(tt);
			}
			
			set.retainAll(ts);
			if (set.size() != 0) {
				s.addAll(ts);
				break;
			}
			if (i == in.size() - 1) {
				in.add(ts);
			}
		}

	}

}
