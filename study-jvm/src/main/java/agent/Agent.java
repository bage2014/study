package agent;

import java.lang.instrument.Instrumentation;

/**
 * https://blogs.oracle.com/ouchina/javaagent
 */
public class Agent {

    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("=========premain方法执行========");
        ClassLogger transformer = new ClassLogger();
        instrumentation.addTransformer(transformer);
    }

}