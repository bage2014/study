package com.bage.study.java.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceMain {

    public static void main(String[] args) {

        Object obj = new Object();

        SoftReference<Object> obj1 = new SoftReference<>(obj);

        WeakReference<Object> obj2 = new WeakReference<>(obj);

        ReferenceQueue<? super Object> queue = new ReferenceQueue<>();
        PhantomReference<Object> obj3 = new PhantomReference<>(obj,queue);

    }

}
