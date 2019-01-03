package com.bage.study.java.tryfinally;

public class TryFinally {

    public static void main(String[] args) {

        System.out.println( work1());
        System.out.println( work2());
        System.out.println( work3());
        System.out.println( work4());
        System.out.println( work5());
        System.out.println( work6());

    }

    private static int work1() {
        int n = 1;
        try {
            return n;
        } finally {
            n = 2;
            return n;
        }
    }

    private static int work2() {
        int n = 1;
        try {
            return n; // 保存现场，不会在改变引用
        } finally {
            n = 2;
        }
    }

    private static TempObj work3() {
        TempObj obj = new TempObj(1);
        try {
            return obj; // 保存现场，不会在改变引用
        } finally {
            obj.setValue(2);
        }
    }

    private static TempObj work4() {
        TempObj obj = new TempObj(1);
        try {
            return obj; // 保存现场，不会在改变引用
        } finally {
            obj  = new TempObj(2);
        }
    }


    private static TempObj work5() {
        TempObj obj = new TempObj(1);
        try {
            return obj; // 保存现场，不会在改变引用
        } finally {
            obj  = new TempObj(2);
            return obj;
        }
    }


    private static Integer work6() {
        Integer obj = 3;
        try {
            return obj; // 保存现场，不会在改变引用
        } finally {
            obj  = 4;
        }
    }

}
