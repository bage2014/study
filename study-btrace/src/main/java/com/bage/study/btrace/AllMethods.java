package com.bage.study.btrace;

//import org.openjdk.btrace.core.annotations.*;
//import static org.openjdk.btrace.core.BTraceUtils.*;
//
///**
// * This script traces method entry into every method of
// * every class in javax.swing package! Think before using
// * this script -- this will slow down your app significantly!!
// */
//@BTrace public class AllMethods {
//    @OnMethod(
//        clazz="/javax\\.swing\\..*/",
//        method="/.*/"
//    )
//    public static void m(@ProbeClassName String probeClass, @ProbeMethodName String probeMethod) {
//        print(Strings.strcat("entered ", probeClass));
//        println(Strings.strcat(".", probeMethod));
//    }
//}