package com.bage.study.call.trace.asm;

import java.io.PrintWriter;
import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;

class ClassAdapter extends ClassVisitor implements Opcodes {

	public ClassAdapter(int api, final ClassVisitor cv) {
		super(api, cv);
	}

	public void visit(final int version, final int access, final String name, final String signature,
			final String superName, final String[] interfaces) {
		if (Premain.PRINT_DEBUG) {
			System.out.println("=== Processing class: " + name + " " + signature);
		}
		
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {
		if (Premain.PRINT_DEBUG) {
			System.out.println(
					"*** Processing method: " + name + " " + desc + " " + signature + " " + Arrays.toString(exceptions));
		}
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
//		if (name.equals("<init>")) {
//			return mv;
//		}
		return mv == null ? null : new MethodCallPrintMethodAdapter(super.api, mv);
	}
}