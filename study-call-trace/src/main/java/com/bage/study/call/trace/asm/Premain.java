package com.bage.study.call.trace.asm;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.regex.Pattern;

/**
 * 
 * <p>When provided with a premain method, the JVM will first execute the premain before the main method.
 * This premain method adds a ClassFileTransformer to the JVM's instrumentation that selectively
 * modifies the source class files to print method execution information. This information includes
 * the signature, arguments, and return value for each called method. All primitives and objects are
 * written to standard error with their respective specializations of the print methods. The only
 * exception to this is arrays where the Arrays.toString method is first called. This is not applied
 * recursively.
 * 
 * <p>Instrumentation is performed only on methods called in classes matching one of the regular expressions
 * stored in the "callerFilters" array. This is set at compile time and thus any changes require 
 * recompiling. This filter is important since non-trivial programs will call many methods and quickly
 * create an overwhelming amount of tracing. Furthermore, it is essential that the java.io.PrintStream
 * class is never instrumented since this would result in the print calls to display the instrumentation
 * themselves being instrumented thus calling print recursively.
 * 
 * <p>This project must be built with maven to ensure the jar file's manifest specifies this class as
 * containing the premain method. This can be done by running "mvn package" in the project directory
 * or by running the "package" goal from the Eclipse "maven build..." run as option. This will produce
 * two jar files in the target directory. The jar file ending with "jar-with-dependencies" is the one
 * to select when using this code.
 * 
 * <p>To use this project for instrumenting code it is necessary to inform the JVM to search the exported
 * jar file. This is done by using the "javaagent" argument. The proper syntax for this argument is
 * "-javaagent:path/to/file.jar".
 *
 * <p>This syntax of the printed instrumentation is as follows:
 * <blockquote><pre>
 * CALLING: package1/package2/../Class methodName (argType0argType1...)returnType
 * |--> Arg 0: val0
 * |--> Arg 1: val1
 * ...
 * |--> Return: ret
 * </pre></blockquote>
 * 
 * <p>A special compact notation is used for displaying type names.
 * <table><tr><th>Type</th><th>Representation</th></tr>
 * <tr><td>int</td><td>I</td></tr>
 * <tr><td>double</td><td>D</td></tr>
 * </table>
 * 
 * <p>A complete example of an instrumentation trace follows:
 * <blockquote><pre>
 * CALLING: edu/unc/cs/asm_test/ASMTest add (ID)D
 * |--> Arg 0: 1
 * |--> Arg 1: 2.0
 * |--> Return: 3.0
 * </pre></blockquote>
 */
public class Premain {

	// com.bage.study.call.trace.asm
	private static final String[] callerFilters = {"com/bage/study/call/trace/asm/.*", "mp/.*", "grail/.*", "main/.*"};
	private static final String[] calledFilters = {"com/bage/study/call/trace/asm/.*", "mp/.*", "grail/.*", "main/.*"};
	public static boolean printThread = false;
	
	public static final boolean PRINT_DEBUG = false;
	public static final boolean PRINT_NEW_BYTECODE = false;
	
	private static Pattern[] patterns;
	
	static {
		patterns = new Pattern[callerFilters.length];
		for (int i = 0; i < callerFilters.length; i ++) {
			patterns[i] = Pattern.compile(callerFilters[i]);
		}
	}
	
	private static boolean doesMatch(String classname) {
		if (classname == null) {
			return false;
		}
		for(Pattern p : patterns) {
			if (p.matcher(classname).matches()) {
				return true;
			}
		}
		return false;
	}
	
	public static void premain(String agentArgs, Instrumentation inst) {
//		System.out.println("premain");
		inst.addTransformer(new ClassFileTransformer() {
//            @Override
			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
					ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//				System.out.println(className);
				try {
					if (doesMatch(className)) {
						if (PRINT_DEBUG) {
							System.out.println("=== Begin instrumenting code ===\n");
						}
						CustomClassWriter cr = new CustomClassWriter(classfileBuffer);
						byte[] ret = cr.printMethods();

						if (PRINT_DEBUG) {
							System.out.println("\n=== Finished instrumenting code ===");
						}
						if (PRINT_NEW_BYTECODE) {
							System.out.println("=== Begin printing code ===\n");
							CustomClassWriter cr2 = new CustomClassWriter(ret);
							cr2.printBytecode();
						}

						if (PRINT_DEBUG) {
							System.out.println("\n=== Finished printing code ===");
						}
						return ret;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return classfileBuffer;
			}
		});
	}
}