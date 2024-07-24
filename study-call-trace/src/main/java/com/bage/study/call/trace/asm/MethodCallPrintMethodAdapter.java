package com.bage.study.call.trace.asm;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class MethodCallPrintMethodAdapter extends MethodVisitor {
	private int curUsed;

	public MethodCallPrintMethodAdapter(int api, MethodVisitor methodVisitor) {
		super(api, methodVisitor);
//		System.out.println("*** NEW METHOD ***");
		curUsed = 0;
	}
	
	@Override
	public void visitParameter(String name, int access) {
		if (Premain.PRINT_DEBUG) { 
			System.out.println("Parameter: " + name + " - " + access);
		}
		super.visitParameter(name, access);
	}
	
	@Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
		switch (type) {
		case Opcodes.F_NEW:
//			System.out.println("FRAME - NEW");
		case Opcodes.F_FULL:
//			if (type != Opcodes.F_NEW) {
//				System.out.println("FRAME - FULL");
//			}
			curUsed = nLocal;
			if (local != null) {
				for (Object localType : local) {
					if (localType == Opcodes.LONG || localType == Opcodes.DOUBLE) {
						curUsed ++;
					}
				}
			}
			break;
		case Opcodes.F_CHOP:
//			System.out.println("FRAME - CHOP");
			curUsed -= nLocal;
			if (local != null) {
				for (Object localType : local) {
					if (localType == Opcodes.LONG || localType == Opcodes.DOUBLE) {
						curUsed --;
					}
				}
			}
			break;
		case Opcodes.F_APPEND:
//			System.out.println("FRAME - APPEND");
			curUsed += nLocal;
			if (local != null) {
				for (Object localType : local) {
					if (localType == Opcodes.LONG || localType == Opcodes.DOUBLE) {
						curUsed ++;
					}
				}
			}
			break;
		}
		if (curUsed < 0) {
			curUsed = 0;
		}
//		System.out.println("nLocal: " + nLocal);
//		System.out.println("local: " + Arrays.toString(local));
//		if (local != null) {
//			Arrays.stream(local).forEach(l -> System.out.println(localToString(l)));
//		}
//		System.out.println("Used: " + curUsed);
        super.visitFrame(type, nLocal, local, nStack, stack);
    }
	
	@Override
	public void visitVarInsn(int opcode, int var) {
		int end = var + ((opcode == Opcodes.LSTORE || opcode == Opcodes.DSTORE || opcode == Opcodes.LLOAD || opcode == Opcodes.DLOAD) ? 1 : 0);
		if (end > curUsed) {
			curUsed = end;
//			System.out.println("Used: " + curUsed);
		}
		super.visitVarInsn(opcode, var);
	}
	
	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
//		System.out.println("visiting field: " + owner + " " + name + " " + descriptor);
		super.visitFieldInsn(opcode, owner, name, descriptor);
	}
	
	@Override
	public void visitLdcInsn(Object value) {
//		System.out.println("load constant: " + value);
		super.visitLdcInsn(value);
	}

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
    	if (Premain.PRINT_DEBUG) {
    		System.out.println("visiting method: " + owner + " " + name + " " + descriptor);
    	}
        super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
        super.visitLdcInsn("CALLING: " + owner + " " + name + " " + descriptor);
        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//        int[] types = parseDescriptor(descriptor);
//        System.out.println(Arrays.toString(types));
        
        String[] typeStrs = splitDescriptor(descriptor);
        int[] types = descriptorsToTypes(typeStrs);
        saveArgs(types);
        printArgs(typeStrs, types);
        restoreArgs(types);
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);  
        printRet(descriptor);
    }
    
    @Override
    public void visitEnd() {
    	visitFrame(-1,-1,null,-1,null);
    	super.visitEnd();
    }
    
    private void printRet(String descriptor) {
    	String retType = descriptor.substring(descriptor.indexOf(')')+1);
    	int type = descriptorToRetType(descriptor);
    	// ret header
		super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
        super.visitLdcInsn("|--> Return: ");
        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);


    	// save ret
    	switch (type) {
    	case Type.INT:
    	case Type.BOOLEAN:
    	case Type.CHAR:
    	case Type.BYTE:
    		super.visitVarInsn(Opcodes.ISTORE, curUsed + 1);
    		break;
    	case Type.DOUBLE:
    		super.visitVarInsn(Opcodes.DSTORE, curUsed + 1);
    		break;
    	case Type.LONG:
    		super.visitVarInsn(Opcodes.LSTORE, curUsed + 1);
    		break;
    	case Type.ARRAY:
    	case Type.OBJECT:
    		super.visitVarInsn(Opcodes.ASTORE, curUsed + 1);
    		break;
    	}
    	
    	// prepare print ret
		super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");

    	// print ret
    	switch (type) {
    	case Type.VOID:
    		super.visitLdcInsn("void");
    		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    		break;
    	case Type.INT:
    	case Type.BYTE:
    	case Type.SHORT:
    		super.visitVarInsn(Opcodes.ILOAD, curUsed + 1);
    		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
    		super.visitVarInsn(Opcodes.ILOAD, curUsed + 1);
    		break;
    	case Type.BOOLEAN:
    		super.visitVarInsn(Opcodes.ILOAD, curUsed + 1);
    		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V", false);
    		super.visitVarInsn(Opcodes.ILOAD, curUsed + 1);
    		break;
    	case Type.CHAR:
    		super.visitVarInsn(Opcodes.ILOAD, curUsed + 1);
    		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(C)V", false);
    		super.visitVarInsn(Opcodes.ILOAD, curUsed + 1);
    		break;
    	case Type.FLOAT:
    		super.visitVarInsn(Opcodes.FLOAD, curUsed + 1);
    		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(F)V", false);
    		super.visitVarInsn(Opcodes.FLOAD, curUsed + 1);
    		break;
    	case Type.DOUBLE:
    		super.visitVarInsn(Opcodes.DLOAD, curUsed + 1);
    		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(D)V", false);
    		super.visitVarInsn(Opcodes.DLOAD, curUsed + 1);
    		break;
    	case Type.LONG:
    		super.visitVarInsn(Opcodes.LLOAD, curUsed + 1);
    		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(L)V", false);
    		super.visitVarInsn(Opcodes.LLOAD, curUsed + 1);
    		break;
    	case Type.ARRAY:
    		super.visitVarInsn(Opcodes.ALOAD, curUsed + 1);
    		String arrType = descriptorToNormalizedArr(retType);
    		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util.Arrays", "toString", "("+arrType+")V", false);
    		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
    		super.visitVarInsn(Opcodes.ALOAD, curUsed + 1);
    		break;
    	case Type.OBJECT:
    		super.visitVarInsn(Opcodes.ALOAD, curUsed + 1);
    		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
    		super.visitVarInsn(Opcodes.ALOAD, curUsed + 1);
    		break;
    	}
    }
    
    private static String descriptorToNormalizedArr(String descriptor) {
    	int arrType = descriptorToArrType(descriptor);
    	switch (arrType) {
    	case Type.BOOLEAN:
    		return "[Z";
		case Type.CHAR:
			return "[C";
		case Type.BYTE:
			return "[B";
		case Type.SHORT:
			return "[S";
		case Type.INT:
			return "[I";
		case Type.FLOAT:
			return "[F";
		case Type.LONG:
			return "[J";
		case Type.DOUBLE:
			return "[D";
		case Type.ARRAY:
		case Type.OBJECT:
		default:
			return "[Ljava/lang/Object;";
    	}
    }
    
    private static int descriptorToRetType(String descriptor) {
    	String ret = descriptor.substring(descriptor.indexOf(')') + 1);
    	switch (ret.charAt(0)) {
 		case 'B':
			return Type.BYTE;
		case 'C':
			return Type.CHAR;
		case 'D':
			return Type.DOUBLE;
		case 'F':
			return Type.FLOAT;
		case 'I':
			return Type.INT;
		case 'J':
			return Type.LONG;
		case 'S':
			return Type.SHORT;
		case 'Z':
			return Type.BOOLEAN;
		case 'L':
			return Type.OBJECT;
		case '[':
			return Type.ARRAY;
		default:
			return Type.VOID;
		}
    }
    
    private static int[] parseDescriptor(String descriptor) {
    	List<Integer> types = new ArrayList<>();
    	
    	descriptor = descriptor.substring(1, descriptor.lastIndexOf(')'));
    	
    	int cur = 0;
    	int type = Type.VOID;
    	boolean skip = false;
    	boolean skipNext = false;
    	
    	while (cur < descriptor.length()) {
    		skip = skipNext;
    		skipNext = false;
    		switch (descriptor.charAt(cur)) {
     		case 'B':
    			type = Type.BYTE;
    			break;
    		case 'C':
    			type = Type.CHAR;
    			break;
    		case 'D':
    			type = Type.DOUBLE;
    			break;
    		case 'F':
    			type = Type.FLOAT;
    			break;
    		case 'I':
    			type = Type.INT;
    			break;
    		case 'J':
    			type = Type.LONG;
    			break;
    		case 'S':
    			type = Type.SHORT;
    			break;
    		case 'Z':
    			type = Type.BOOLEAN;
    			break;
    		case 'L':
    			type = Type.OBJECT;
    			cur = descriptor.indexOf(';', cur);
    		case '[':
    			type = Type.ARRAY;
    			skipNext = true;
    			break;
    		}
    		if (!skip) {
    			types.add(type);
    		}
    		cur ++;
    	}
    	
    	return types.stream().mapToInt(i->(int)i).toArray();
    }
    
    private void saveArgs(int[] types) {
    	for(int i = types.length - 1, offset = 1; i >= 0; i --, offset++) {
    		int type = types[i];
    		switch(type) {
    		case Type.BOOLEAN:
    		case Type.CHAR:
    		case Type.BYTE:
    		case Type.SHORT:
    		case Type.INT:
    			if (Premain.PRINT_DEBUG) {
    				System.out.println("Saving arg (int) " + i + " to " + (curUsed + offset));
    			}
    			super.visitVarInsn(Opcodes.ISTORE, curUsed + offset);
    			break;
    		case Type.FLOAT:
    			if (Premain.PRINT_DEBUG) {
    				System.out.println("Saving arg (float) " + i + " to " + (curUsed + offset));
    			}
    			super.visitVarInsn(Opcodes.FSTORE, curUsed + offset);
    			break;
    		case Type.LONG:
    			if (Premain.PRINT_DEBUG) {
    				System.out.println("Saving arg (long) " + i + " to " + (curUsed + offset));
    			}
    			super.visitVarInsn(Opcodes.LSTORE, curUsed + offset);
    			offset++;
    			break;
    		case Type.DOUBLE:
    			if (Premain.PRINT_DEBUG) {
    				System.out.println("Saving arg (double) " + i + " to " + (curUsed + offset));
    			}
    			super.visitVarInsn(Opcodes.DSTORE, curUsed + offset);
    			offset++;
    			break;
    		case Type.ARRAY:
    		case Type.OBJECT:
    			if (Premain.PRINT_DEBUG) {
    				System.out.println("Saving arg (object) " + i + " to " + (curUsed + offset));
    			}
    			super.visitVarInsn(Opcodes.ASTORE, curUsed + offset);
    			break;
    		}
    	}
    }
    
    private void printArgs(String[] typeStrs, int[] types) {
//    	super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
//        super.visitLdcInsn("Print args");
//        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

    	int offset = types.length;
    	for (int type : types) {
    		if (type == Type.LONG || type == Type.DOUBLE) {
    			offset ++;
    		}
    	}
    	for(int i = 0; i < types.length; i ++, offset--) {
    		int type = types[i];
//    		System.out.println(types[i] + " " + typeStrs[i]);
    		
//    		System.out.println("Adding print for arg " + i);
    		super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
            super.visitLdcInsn("|--> Arg " + i + ": ");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);

            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");

    		switch(type) {
    		case Type.CHAR:
    		case Type.BYTE:
    		case Type.SHORT:
    		case Type.INT:
    		case Type.BOOLEAN:
    			super.visitVarInsn(Opcodes.ILOAD, curUsed + offset);
    			break;
    		case Type.FLOAT:
    			super.visitVarInsn(Opcodes.FLOAD, curUsed + offset);
    			break;
    		case Type.DOUBLE:
            	offset--;
            	super.visitVarInsn(Opcodes.DLOAD, curUsed + offset);
    			break;
    		case Type.LONG:
            	offset--;
            	super.visitVarInsn(Opcodes.LLOAD, curUsed + offset);
    			break;
    		case Type.ARRAY:
    		case Type.OBJECT:
    			super.visitVarInsn(Opcodes.ALOAD, curUsed + offset);
    			break;
    		}
    		
    		switch(type) {
    		case Type.CHAR:
    	        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(C)V", false);
    			break;
    		case Type.BYTE:
    		case Type.SHORT:
    		case Type.INT:
    	        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
    			break;
    		case Type.BOOLEAN:
    	        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V", false);
    			break;
    		case Type.FLOAT:
    	        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(F)V", false);
    			break;
    		case Type.DOUBLE:
    	        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(D)V", false);
    			break;
    		case Type.LONG:
    	        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false);
    			break;
    		case Type.ARRAY:
        		String arrType = descriptorToNormalizedArr(typeStrs[i]);
        		super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/util/Arrays", "toString", "("+arrType+")Ljava/lang/String;", false);
    		case Type.OBJECT:
    	        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
    			break;
    		}
    	}
    }
    
    private void restoreArgs(int[] types) {
    	int offset = types.length;
    	for (int type : types) {
    		if (type == Type.LONG || type == Type.DOUBLE) {
    			offset ++;
    		}
    	}
    	for(int i = 0; i < types.length; i ++, offset--) {
    		int type = types[i];
    		switch(type) {
    		case Type.CHAR:
    		case Type.BYTE:
    		case Type.SHORT:
    		case Type.INT:
    		case Type.BOOLEAN:
    			if (Premain.PRINT_DEBUG) {
    				System.out.println("Restoring arg (int) " + i + " from " + (curUsed + offset));
    			}
    			super.visitVarInsn(Opcodes.ILOAD, curUsed + offset);
    			break;
    		case Type.FLOAT:
    			if (Premain.PRINT_DEBUG) {
    				System.out.println("Restoring arg (float) " + i + " from " + (curUsed + offset));
    			}
    			super.visitVarInsn(Opcodes.FLOAD, curUsed + offset);
    			break;
    		case Type.DOUBLE:
    			offset--;
    			if (Premain.PRINT_DEBUG) {
    				System.out.println("Restoring arg (double) " + i + " from " + (curUsed + offset));
    			}
    			super.visitVarInsn(Opcodes.DLOAD, curUsed + offset);
    			break;
    		case Type.LONG:
    			offset--;
    			if (Premain.PRINT_DEBUG) {
    				System.out.println("Restoring arg (long) " + i + " from " + (curUsed + offset));
    			}
    			super.visitVarInsn(Opcodes.LLOAD, curUsed + offset);
    			break;
    		case Type.ARRAY:
    		case Type.OBJECT:
    			if (Premain.PRINT_DEBUG) {
    				System.out.println("Restoring arg (object) " + i + " from " + (curUsed + offset));
    			}
    			super.visitVarInsn(Opcodes.ALOAD, curUsed + offset);
    			break;
    		}
    	}
    }
    
    private static String localToString(Object local) {
    	if (local == null) {
    		return "null";
    	} else if (local instanceof String) {
    		return descriptorToString((String)local);
    	} else if (local instanceof Integer) {
    		Integer l = (Integer) local;
    		if (l.equals(Opcodes.TOP)) {
    			return "top";
    		} else if (l.equals(Opcodes.INTEGER)) {
    			return "int";
    		} else if (l.equals(Opcodes.FLOAT)) {
    			return "float";
    		} else if (l.equals(Opcodes.DOUBLE)) {
    			return "double";
    		} else if (l.equals(Opcodes.LONG)) {
    			return "long";
    		} else if (l.equals(Opcodes.NULL)) {
    			return "null";
    		} else if (l.equals(Opcodes.UNINITIALIZED_THIS)) {
    			return "unititialized this";
    		} else {
    			return "unknown";
    		}
    	} else {
    		return "unknown";
    	}
    }
    
    private static String[] splitDescriptor(String descriptor) {
    	List<String> types = new ArrayList<>();
    	descriptor = descriptor.substring(1, descriptor.lastIndexOf(')'));
    	
    	int cur = 0;
    	boolean skip = false;
    	boolean skipNext = false;
    	StringBuilder type = new StringBuilder();
    	
    	while (cur < descriptor.length()) {
    		skip = skipNext;
    		skipNext = false;
    		switch (descriptor.charAt(cur)) {
     		case 'B':
     			type.append("B");
    			break;
    		case 'C':
    			type.append("C");
    			break;
    		case 'D':
    			type.append("D");
    			break;
    		case 'F':
    			type.append("F");
    			break;
    		case 'I':
    			type.append("I");
    			break;
    		case 'J':
    			type.append("J");
    			break;
    		case 'S':
    			type.append("S");
    			break;
    		case 'Z':
    			type.append("Z");
    			break;
    		case 'L':
    			type.append(descriptor.substring(cur, descriptor.indexOf(';')+1));
    			cur = descriptor.indexOf(';', cur);
    			break;
    		case '[':
    			type.append("[");
    			skip = true;
    			skipNext = true;
    			break;
    		}
    		if (!skip) {
    			types.add(type.toString());
    			type.setLength(0);
    		}
    		cur ++;
    	}
    	if (type.length() > 0) {
	    	skip = skipNext;
	    	if (!skip) {
				types.add(type.toString());
				type.setLength(0);
			}
    	}
    	
    	return types.toArray(new String[types.size()]);
    }
    
    private static int[] descriptorsToTypes(String[] descriptors) {
    	return Arrays.stream(descriptors).mapToInt(MethodCallPrintMethodAdapter::descriptorToType).toArray();
    }
    
    private static int descriptorToType(String descriptor) {
    	switch (descriptor.charAt(0)) {
 		case 'B':
			return Type.BYTE;
		case 'C':
			return Type.CHAR;
		case 'D':
			return Type.DOUBLE;
		case 'F':
			return Type.FLOAT;
		case 'I':
			return Type.INT;
		case 'J':
			return Type.LONG;
		case 'S':
			return Type.SHORT;
		case 'Z':
			return Type.BOOLEAN;
		case 'L':
			return Type.OBJECT;
		case '[':
			return Type.ARRAY;
		default:
			return Type.VOID;
		}
    }
    
    private static int descriptorToArrType(String descriptor) {
    	return descriptorToType(descriptor.substring(1));
    }
    
    private static String descriptorToString(String descriptor) {
    	switch (descriptor.charAt(0)) {
    	case 'B':
			return "byte";
		case 'C':
			return "char";
		case 'D':
			return "double";
		case 'F':
			return "float";
		case 'I':
			return "int";
		case 'J':
			return "long";
		case 'S':
			return "short";
		case 'Z':
			return "boolean";
		case 'L':
			return descriptor.substring(1, descriptor.indexOf(';', 1));
		case '[':
			return descriptorToString(descriptor.substring(1)) + "[]";
		default:
			return descriptor;
		}
    }
}
