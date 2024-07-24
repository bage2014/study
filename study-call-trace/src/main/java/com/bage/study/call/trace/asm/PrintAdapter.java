package com.bage.study.call.trace.asm;

import java.io.PrintWriter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class PrintAdapter extends ClassVisitor {
    public PrintAdapter(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
//        System.out.println("Processing method: " + name + " " + desc + " " + signature);
        Printer p = new Textifier(Opcodes.ASM6) {
            @Override
            public void visitMethodEnd() {
                print(new PrintWriter(System.out)); // print it after it has been visited
            }
        };
        return new TraceMethodVisitor(mv, p);
    }
}
