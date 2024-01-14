package com.bage.study.java.type;

public class ByteTest {

    public static void main(String[] args) {
        byte a = 127;
        byte b = 127;
//        b = a + b; // error : cannot convert from int to byte
        // (因为 a+b 操作会将 a、b 提升为 int 类型，所以将 int 类型赋值给 byte 就会编译出错)


        b += a; // ok

        System.out.println(b);


        Long aa = 123L;
        Long bb = 123L;
        bb = aa + bb;
        System.out.println(bb);
    }

}
