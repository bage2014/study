package com.bage.study.java.type;

public class FloatTest {

    public static void main(String[] args) {

        System.out.println(3*0.1 == 0.3); // false
//        3*0.1 == 0.3 将会返回什么? true 还是 false?

        System.out.println(3*0.1); // 0.30000000000000004

        double aa = 3*0.1;
        System.out.println(aa); // 0.30000000000000004



        double bb = 0.1;
        System.out.println(bb * 3); // 0.30000000000000004

    }

}
