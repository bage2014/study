package com.bage.study.java.type;

public class SuperTest {
    public static void main(String args[]){
        Info3<String> i1 = new Info3<String>() ;        // 声明String的泛型对象
        Info3<Object> i2 = new Info3<Object>() ;        // 声明Object的泛型对象
        i1.setVar("hello") ;
        i2.setVar(new Object()) ;
//        i2.setVar(new Integer(1)) ;
        fun(i1) ;
        fun(i2) ;
    }
    public static void fun(Info3<? super String> temp){    // 只能接收String或Object类型的泛型，String类的父类只有Object类
        System.out.print(temp + ", ") ;
    }


    private static
    class Info3<T>{
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
