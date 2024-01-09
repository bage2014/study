package com.bage.study.java.type;


public class ExtendsTest {
    public static void main(String args[]){
        Info2<Integer> i1 = new Info2<Integer>() ;
        i1.setVar(123);
        System.out.println(i1);
        // 声明Integer的泛型对象
    }

    private static class Info2<T extends Number>{    // 此处泛型只能是数字类型
        private T var ;        // 定义泛型变量
        public void setVar(T var){
            this.var = var ;
        }
        public T getVar(){
            return this.var ;
        }
        public String toString(){    // 直接打印
            return this.var.toString() ;
        }
    }
}
