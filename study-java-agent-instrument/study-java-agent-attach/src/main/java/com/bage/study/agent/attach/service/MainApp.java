package com.bage.study.agent.attach.service;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

/**
 * Hello world!
 */
public class MainApp {
    public static void main(String[] args) throws IOException, AttachNotSupportedException {
        System.out.println("Hello World!");
        // VirtualMachine等相关Class位于JDK的tools.jar
        VirtualMachine vm = null;
        try {
            vm = VirtualMachine.attach("134104");  // 1234表示目标JVM进程pid
            vm.loadAgent("D:\\doc\\bage\\java-agent\\study-java-agent\\study-java-agent\\study-java-agent-hotload-service\\target\\study-java-agent-hotload-service-1.0-SNAPSHOT-jar-with-dependencies.jar");

            Thread.sleep(60 * 1000L);
        } catch (AgentLoadException e) {
            e.printStackTrace();
        } catch (AgentInitializationException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (vm != null) {
                vm.detach();
            }
        }
    }
}
